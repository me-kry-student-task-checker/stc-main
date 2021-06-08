package hu.me.iit.malus.thesis.course.service.impl;

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
import hu.me.iit.malus.thesis.dto.Teacher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
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
     *
     * @return
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
     *
     * @return
     */
    @Override
    public CourseOverviewDto edit(CourseModificationDto dto, String editorsEmail) throws ForbiddenCourseEditException {
        Course course = courseRepository.findById(dto.getId()).orElseThrow(CourseNotFoundException::new);
        if (!course.getCreator().getEmail().equals(editorsEmail)) {
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
     *
     * @return
     */
    @Override
    public CourseFullDetailsDto get(Long courseId, String userEmail) throws CourseNotFoundException {
        Course course = courseRepository.findById(courseId).orElseThrow(CourseNotFoundException::new);
        if (!userClient.isRelated(course.getId())) {
            log.warn("User {} is not realated to this course {}!", userEmail, course);
            throw new CourseNotFoundException();
        }
        //TODO: We should find a way to fire these as async requests
        course.setCreator(userClient.getTeacherByCreatedCourseId(courseId));
        course.setStudents(userClient.getStudentsByAssignedCourseId(courseId));
        course.setTasks(taskClient.getAllTasks(courseId));
        course.setFiles(fileManagementClient.getAllFilesByTagId(hu.me.iit.malus.thesis.dto.Service.COURSE, courseId));
        course.setComments(feedbackClient.getAllCourseComments(courseId));
        log.debug("Course found: {}", courseId);
        return Converter.createCourseFullDetailsDtoFromCourse(course);
    }

    /**
     * {@inheritDoc}
     *
     * @return
     */
    @Override
    public Set<CourseOverviewDto> getAll(String userEmail) {
        Set<Long> relatedCourseIds = userClient.getRelatedCourseIds();
        Set<Course> relatedCourses = courseRepository.findAllByIdIsIn(relatedCourseIds);

        for (Course course : relatedCourses) {
            course.setCreator(userClient.getTeacherByCreatedCourseId(course.getId()));
        }
        log.debug("Get all courses done, total number of courses is {}", relatedCourses.size());
        return relatedCourses.stream().map(Converter::createCourseOverviewDtoFromCourse).collect(Collectors.toSet());
    }

    @Override
    @Transactional
    public void deleteCourse(Long courseId) throws CourseNotFoundException {
        boolean isRelated = userClient.isRelated(courseId);
        if (!isRelated) {
            log.warn("Only the creator can delete a course!");
            throw new ForbiddenCourseEdit();
        }
        if (courseRepository.existsById(courseId)) {
            courseRepository.deleteById(courseId);
            userClient.removeCourseIdFromRelatedUserLists(courseId);
            taskClient.removeTasksByCourseId(courseId);
            fileManagementClient.removeFilesByServiceAndTagId(hu.me.iit.malus.thesis.dto.Service.COURSE, courseId);
            feedbackClient.removeCourseCommentsByCourseId(courseId);
            return;
        }
        throw new CourseNotFoundException();
    }
}
