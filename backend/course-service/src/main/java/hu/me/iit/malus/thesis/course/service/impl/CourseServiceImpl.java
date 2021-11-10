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
import hu.me.iit.malus.thesis.course.model.transaction.TransactionCommand;
import hu.me.iit.malus.thesis.course.model.transaction.TransactionCommandListFactory;
import hu.me.iit.malus.thesis.course.repository.CourseRepository;
import hu.me.iit.malus.thesis.course.service.CourseService;
import hu.me.iit.malus.thesis.course.service.converters.Converter;
import hu.me.iit.malus.thesis.course.service.exception.CourseDeleteRollbackException;
import hu.me.iit.malus.thesis.course.service.exception.CourseNotFoundException;
import hu.me.iit.malus.thesis.course.service.exception.ForbiddenCourseEditException;
import hu.me.iit.malus.thesis.dto.ServiceType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private final TransactionCommandListFactory factory;

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
        List<TransactionCommand> transactionCommandList = factory.create(course);
        try {
            // Prepare phase
            transactionCommandList.forEach(TransactionCommand::prepare);
            // Commit Phase
            transactionCommandList.forEach(TransactionCommand::commit);
            log.debug("Removed course with id {} and everything connected to it using 2PC!", courseId);
        } catch (FeignException e) {
            // Rollback Phase
            transactionCommandList.forEach(TransactionCommand::rollback);
            throw new CourseDeleteRollbackException(course.getId(), e); // to trigger transactional annotation rollback
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
