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
    @Transactional
    public void deleteCourse(Long courseId) throws CourseNotFoundException, ForbiddenCourseEditException {
        Course course = courseRepository.findByIdAndRemovedFalse(courseId).orElseThrow(CourseNotFoundException::new);
        if (!userClient.isRelated(courseId)) {
            log.warn("User is not related to this course {}!", course);
            throw new ForbiddenCourseEditException();
        }
        log.info("start");
        course.setRemoved(true);
        courseRepository.save(course);
        fillCourseDetails(course);
        log.info("course details got");

        final String emptyKey = "empty";
        String taskTransactionKey = emptyKey;
        String taskCommentTransactionKey = emptyKey;
        String taskCommentFileTransactionKey = emptyKey;
        String taskFileTransactionKey = emptyKey;
        String courseCommentTransactionKey = emptyKey;
        String courseCommentFileTransactionKey = emptyKey;
        String courseFileTransactionKey = emptyKey;
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
            log.info("try start");
            // Prepare phase
            // Removal of tasks and everything connected to it
            if (!course.getTasks().isEmpty()) {
                log.info("task prepare {} with courseId: {}", taskTransactionKey, courseId);
                taskTransactionKey = taskClient.prepareRemoveTaskByCourseId(courseId);
            }
            if (!taskIds.isEmpty()) {
                log.info("task comment prepare {} with taskIds: {}", taskCommentTransactionKey, taskIds);
                taskCommentTransactionKey = feedbackClient.prepareRemoveTaskCommentsByTaskIds(taskIds);
                log.info("task files prepare {} with taskIds: {}", taskFileTransactionKey, taskIds);
                taskFileTransactionKey = fileManagementClient.prepareRemoveFilesByServiceTypeAndTagIds(ServiceType.TASK, taskIds);
            }
            if (!taskCommentIds.isEmpty()) {
                log.info("task comment files prepare {} with taskCommentIds: {}", taskCommentFileTransactionKey, taskCommentIds);
                taskCommentFileTransactionKey = fileManagementClient.prepareRemoveFilesByServiceTypeAndTagIds(ServiceType.FEEDBACK, taskCommentIds);
            }
            // Removal of course comments and everything connected to it
            if (!course.getComments().isEmpty()) {
                log.info("course comment prepare {} with courseId: {}", courseCommentTransactionKey, courseId);
                courseCommentTransactionKey = feedbackClient.prepareRemoveCourseCommentsByCourseId(courseId);
            }
            if (!courseCommentIds.isEmpty()) {
                log.info("course comment file prepare {} with courseCommentIds: {}", courseCommentFileTransactionKey, courseCommentIds);
                courseCommentFileTransactionKey = fileManagementClient.prepareRemoveFilesByServiceTypeAndTagIds(ServiceType.FEEDBACK, courseCommentIds);
            }
            // Removal of course files
            if (!course.getFiles().isEmpty()) {
                log.info("course file prepare {} with courseId: {}", courseFileTransactionKey, courseId);
                courseFileTransactionKey = fileManagementClient.prepareRemoveFilesByServiceTypeAndTagIds(ServiceType.COURSE, List.of(courseId));
            }

            // Commit Phase
            log.info("task commit tk: {}", taskTransactionKey);
            taskClient.commitRemoveTaskByCourseId(taskTransactionKey);
            log.info("task comment commit tk: {}", taskCommentTransactionKey);
            feedbackClient.commitRemoveTaskCommentsByTaskIds(taskCommentTransactionKey);
            log.info("task comment files commit tk: {}", taskCommentFileTransactionKey);
            fileManagementClient.commitRemoveFilesByServiceTypeAndTagIds(taskCommentFileTransactionKey);
            log.info("task files commit tk: {}", taskFileTransactionKey);
            fileManagementClient.commitRemoveFilesByServiceTypeAndTagIds(taskFileTransactionKey);
            log.info("course comment commit tk: {}", courseCommentTransactionKey);
            feedbackClient.commitRemoveCourseCommentsByCourseId(courseCommentTransactionKey);
            log.info("course comment file commit tk: {}", courseCommentFileTransactionKey);
            fileManagementClient.commitRemoveFilesByServiceTypeAndTagIds(courseCommentFileTransactionKey);
            log.info("course file commit tk: {}", courseFileTransactionKey);
            fileManagementClient.commitRemoveFilesByServiceTypeAndTagIds(courseFileTransactionKey);
            log.info("Removed course with id {} and everything connected to it using 2PC!", courseId);
        } catch (FeignException e) {
            // Rollback Phase
            taskClient.rollbackRemoveTaskByCourseId(taskTransactionKey);
            feedbackClient.rollbackRemoveTaskCommentsByTaskIds(taskCommentTransactionKey);
            fileManagementClient.rollbackRemoveFilesByServiceTypeAndTagIds(taskCommentFileTransactionKey);
            fileManagementClient.rollbackRemoveFilesByServiceTypeAndTagIds(taskFileTransactionKey);
            feedbackClient.rollbackRemoveCourseCommentsByCourseId(courseCommentTransactionKey);
            fileManagementClient.rollbackRemoveFilesByServiceTypeAndTagIds(courseCommentFileTransactionKey);
            fileManagementClient.rollbackRemoveFilesByServiceTypeAndTagIds(courseFileTransactionKey);
            throw e; // to trigger transactional annotation rollback
        }
    }

    private void fillCourseDetails(Course course) {
        course.setCreator(userClient.getTeacherByCreatedCourseId(course.getId()));
        course.setStudents(userClient.getStudentsByAssignedCourseId(course.getId()));
        course.setTasks(taskClient.getAllTasks(course.getId()));
        course.setFiles(fileManagementClient.getAllFilesByTagId(ServiceType.COURSE, course.getId()));
        course.setComments(feedbackClient.getAllCourseComments(course.getId()));
    }
}
