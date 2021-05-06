package hu.me.iit.malus.thesis.course.service.impl;

import hu.me.iit.malus.thesis.course.client.FeedbackClient;
import hu.me.iit.malus.thesis.course.client.FileManagementClient;
import hu.me.iit.malus.thesis.course.client.TaskClient;
import hu.me.iit.malus.thesis.course.client.UserClient;
import hu.me.iit.malus.thesis.course.model.Course;
import hu.me.iit.malus.thesis.course.model.exception.ForbiddenCourseEdit;
import hu.me.iit.malus.thesis.course.repository.CourseRepository;
import hu.me.iit.malus.thesis.course.service.CourseService;
import hu.me.iit.malus.thesis.course.service.exception.CourseNotFoundException;
import hu.me.iit.malus.thesis.dto.Teacher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Date;
import java.util.Optional;
import java.util.Set;

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
    public Course create(Course course, String creatorsEmail) {
        course.setCreationDate(Date.from(Instant.now()));
        Course newCourse = courseRepository.save(course);
        Teacher teacher = userClient.saveCourseCreation(newCourse.getId());
        newCourse.setCreator(teacher);
        log.debug("Created course: {}", newCourse.getId());
        return newCourse;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Course edit(Course newCourse, String editorsEmail) {
        boolean isRelated = userClient.isRelated(newCourse.getId());
        if (!isRelated) {
            log.warn("Creator of this newCourse {} is not the editor: {}!", newCourse, editorsEmail);
            throw new ForbiddenCourseEdit();
        }
        Optional<Course> optCourse = courseRepository.findById(newCourse.getId());
        if (optCourse.isPresent()) {
            Course course = optCourse.get();
            log.debug("Modified course: {} to: {}", course, newCourse);
            course.setName(newCourse.getName());
            course.setDescription(newCourse.getDescription());
            return courseRepository.save(course);
        } else {
            log.warn("No course found with this id: {}", newCourse.getId());
            throw new CourseNotFoundException();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Course get(Long courseId, String userEmail) throws CourseNotFoundException {
        Optional<Course> optCourse = courseRepository.findById(courseId);

        if (optCourse.isPresent()) {
            Course course = optCourse.get();
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
            return course;
        } else {
            log.warn("No course found with this id: {}", courseId);
            throw new CourseNotFoundException();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<Course> getAll(String userEmail) {
        Set<Long> relatedCourseIds = userClient.getRelatedCourseIds();
        Set<Course> relatedCourses = courseRepository.findAllByIdIsIn(relatedCourseIds);

        for (Course course : relatedCourses) {
            course.setCreator(userClient.getTeacherByCreatedCourseId(course.getId()));
        }
        log.debug("Get all courses done, total number of courses is {}", relatedCourses.size());
        return relatedCourses;
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
