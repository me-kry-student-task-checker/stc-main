package hu.me.iit.malus.thesis.course.service.impl;

import feign.FeignException;
import hu.me.iit.malus.thesis.course.client.FeedbackClient;
import hu.me.iit.malus.thesis.course.client.FileManagementClient;
import hu.me.iit.malus.thesis.course.client.TaskClient;
import hu.me.iit.malus.thesis.course.client.UserClient;
import hu.me.iit.malus.thesis.course.controller.dto.CourseCreateDto;
import hu.me.iit.malus.thesis.course.controller.dto.CourseFullDetailsDto;
import hu.me.iit.malus.thesis.course.controller.dto.CourseModificationDto;
import hu.me.iit.malus.thesis.course.controller.dto.CourseOverviewDto;
import hu.me.iit.malus.thesis.course.model.Course;
import hu.me.iit.malus.thesis.course.repository.CourseRepository;
import hu.me.iit.malus.thesis.course.service.CourseService;
import hu.me.iit.malus.thesis.course.service.converters.Converter;
import hu.me.iit.malus.thesis.course.service.exception.CourseDeleteRollbackException;
import hu.me.iit.malus.thesis.course.service.exception.CourseNotFoundException;
import hu.me.iit.malus.thesis.course.service.exception.ForbiddenCourseEditException;
import hu.me.iit.malus.thesis.dto.CourseComment;
import hu.me.iit.malus.thesis.dto.ServiceType;
import hu.me.iit.malus.thesis.dto.Task;
import hu.me.iit.malus.thesis.dto.TaskComment;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Default implementation for Course service.
 *
 * @author Attila Szőke
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;
    private final TaskClient taskClient;
    private final FeedbackClient feedbackClient;
    private final UserClient userClient;
    private final FileManagementClient fileManagementClient;

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public CourseOverviewDto create(CourseCreateDto dto, String creatorsEmail) {
        Course course = Converter.createCourseFromCourseCreateDto(dto);
        course.setCreationDate(new Date());
        course = courseRepository.save(course);
        userClient.saveCourseCreation(course.getId());
        log.debug("Created course: {}", course);
        return Converter.createCourseOverviewDtoFromCourse(course);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CourseOverviewDto edit(CourseModificationDto dto, String editorsEmail) throws ForbiddenCourseEditException, CourseNotFoundException {
        Course course = courseRepository.findByIdAndRemovedFalse(dto.getId()).orElseThrow(CourseNotFoundException::new);
        if (!userClient.isRelated(course.getId())) {
            log.warn("Creator of this course {} is not the editor: {}!", course, editorsEmail);
            throw new ForbiddenCourseEditException();
        }
        course.setName(dto.getName());
        course.setDescription(dto.getDescription());
        log.debug("Modified course: {}!", course);
        return Converter.createCourseOverviewDtoFromCourse(courseRepository.save(course));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CourseFullDetailsDto get(Long courseId) throws CourseNotFoundException {
        Course course = courseRepository.findByIdAndRemovedFalse(courseId).orElseThrow(CourseNotFoundException::new);
        if (!userClient.isRelated(courseId)) {
            log.warn("User is not related to this course {}!", course);
            throw new CourseNotFoundException();
        }
        fillCourseDetails(course);
        log.debug("Course found: {}", courseId);
        return Converter.createCourseFullDetailsDtoFromCourse(course);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<CourseOverviewDto> getAll(String userEmail) {
        Set<Long> relatedCourseIds = userClient.getRelatedCourseIds();
        Set<Course> relatedCourses = courseRepository.findAllByIdIsInAndRemovedFalse(relatedCourseIds);

        for (Course course : relatedCourses) {
            course.setCreator(userClient.getTeacherByCreatedCourseId(course.getId()));
        }
        log.debug("Get all courses done, total number of courses is {}", relatedCourses.size());
        return relatedCourses.stream().map(Converter::createCourseOverviewDtoFromCourse).collect(Collectors.toSet());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(rollbackFor = {CourseDeleteRollbackException.class})
    public void deleteCourse(Long courseId) throws CourseNotFoundException, ForbiddenCourseEditException, CourseDeleteRollbackException {
        Course course = courseRepository.findByIdAndRemovedFalse(courseId).orElseThrow(CourseNotFoundException::new);
        if (!userClient.isRelated(courseId)) {
            log.debug("User is not related to this course {}!", course);
            throw new ForbiddenCourseEditException();
        }
        course.setRemoved(true);
        courseRepository.save(course);
        fillCourseDetails(course);

        String reason = "";
        String taskTransactionKey = "";
        String taskCommentTransactionKey = "";
        String taskCommentFileTransactionKey = "";
        String taskFileTransactionKey = "";
        String courseCommentTransactionKey = "";
        String courseCommentFileTransactionKey = "";
        String courseFileTransactionKey = "";

        List<Long> taskIds = course.getTasks().stream().map(Task::getId).collect(Collectors.toList());
        List<Long> taskCommentIds = course.getTasks().stream()
                .map(Task::getComments)
                .flatMap(Collection::stream)
                .map(TaskComment::getId)
                .collect(Collectors.toList());
        List<Long> courseCommentIds = course.getComments().stream()
                .map(CourseComment::getId)
                .collect(Collectors.toList());
        try {
            // Prepare phase
            // Removal of tasks and everything connected to it
            if (!course.getTasks().isEmpty()) {
                reason = "PREPARE_TASK_REMOVAL";
                taskTransactionKey = taskClient.prepareRemoveTaskByCourseId(courseId);
            }
            if (!taskIds.isEmpty()) {
                reason = "PREPARE_TASK_COMMENT_REMOVAL";
                taskCommentTransactionKey = feedbackClient.prepareRemoveTaskCommentsByTaskIds(taskIds);
                reason = "PREPARE_TASK_FILE_REMOVAL";
                taskFileTransactionKey = fileManagementClient.prepareRemoveFilesByServiceTypeAndTagIds(ServiceType.TASK, taskIds);
            }
            if (!taskCommentIds.isEmpty()) {
                reason = "PREPARE_TASK_COMMENT_FILE_REMOVAL";
                taskCommentFileTransactionKey = fileManagementClient.prepareRemoveFilesByServiceTypeAndTagIds(ServiceType.FEEDBACK, taskCommentIds);
            }
            // Removal of course comments and everything connected to it
            if (!course.getComments().isEmpty()) {
                reason = "PREPARE_COURSE_COMMENT_REMOVAL";
                courseCommentTransactionKey = feedbackClient.prepareRemoveCourseCommentsByCourseId(courseId);
            }
            if (!courseCommentIds.isEmpty()) {
                reason = "PREPARE_COURSE_COMMENT_FILE_REMOVAL";
                courseCommentFileTransactionKey = fileManagementClient.prepareRemoveFilesByServiceTypeAndTagIds(ServiceType.FEEDBACK, courseCommentIds);
            }
            // Removal of course files
            if (!course.getFiles().isEmpty()) {
                reason = "PREPARE_COURSE_FILE_REMOVAL";
                courseFileTransactionKey = fileManagementClient.prepareRemoveFilesByServiceTypeAndTagIds(ServiceType.COURSE, List.of(courseId));
            }

            // Commit Phase
            if (!taskTransactionKey.isEmpty()) taskClient.commitRemoveTaskByCourseId(taskTransactionKey);
            if (!taskCommentTransactionKey.isEmpty()) feedbackClient.commitRemoveTaskCommentsByTaskIds(taskCommentTransactionKey);
            if (!taskFileTransactionKey.isEmpty()) fileManagementClient.commitRemoveFilesByServiceTypeAndTagIds(taskFileTransactionKey);
            if (!taskCommentFileTransactionKey.isEmpty()) fileManagementClient.commitRemoveFilesByServiceTypeAndTagIds(taskCommentFileTransactionKey);
            if (!courseCommentTransactionKey.isEmpty()) feedbackClient.commitRemoveCourseCommentsByCourseId(courseCommentTransactionKey);
            if (!courseCommentFileTransactionKey.isEmpty())
                fileManagementClient.commitRemoveFilesByServiceTypeAndTagIds(courseCommentFileTransactionKey);
            if (!courseFileTransactionKey.isEmpty()) fileManagementClient.commitRemoveFilesByServiceTypeAndTagIds(courseFileTransactionKey);
            log.debug("Removed course with id {} and everything connected to it using 2PC!", courseId);
        } catch (FeignException e) {
            // Rollback Phase
            if (!taskTransactionKey.isEmpty()) taskClient.rollbackRemoveTaskByCourseId(taskTransactionKey);
            if (!taskCommentTransactionKey.isEmpty()) feedbackClient.rollbackRemoveTaskCommentsByTaskIds(taskCommentTransactionKey);
            if (!taskFileTransactionKey.isEmpty()) fileManagementClient.rollbackRemoveFilesByServiceTypeAndTagIds(taskFileTransactionKey);
            if (!taskCommentFileTransactionKey.isEmpty())
                fileManagementClient.rollbackRemoveFilesByServiceTypeAndTagIds(taskCommentFileTransactionKey);
            if (!courseCommentTransactionKey.isEmpty()) feedbackClient.rollbackRemoveCourseCommentsByCourseId(courseCommentTransactionKey);
            if (!courseCommentFileTransactionKey.isEmpty())
                fileManagementClient.rollbackRemoveFilesByServiceTypeAndTagIds(courseCommentFileTransactionKey);
            if (!courseFileTransactionKey.isEmpty()) fileManagementClient.rollbackRemoveFilesByServiceTypeAndTagIds(courseFileTransactionKey);
            throw new CourseDeleteRollbackException(course.getId(), reason); // to trigger transactional annotation rollback
        }
    }

    /**
     * Fills in the details of a course by making requests to other services.
     *
     * @param course the course
     */
    private void fillCourseDetails(Course course) {
        course.setCreator(userClient.getTeacherByCreatedCourseId(course.getId()));
        course.setStudents(userClient.getStudentsByAssignedCourseId(course.getId()));
        course.setTasks(taskClient.getAllTasks(course.getId()));
        course.setFiles(fileManagementClient.getAllFilesByTagId(ServiceType.COURSE, course.getId()));
        course.setComments(feedbackClient.getAllCourseComments(course.getId()));
    }
}
