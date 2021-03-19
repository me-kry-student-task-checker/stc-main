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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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
    public Course create(Course course, String creatorsEmail) {
        course.setCreationDate(Date.from(Instant.now()));
        Course newCourse = courseRepository.save(course);
        userClient.saveCourseCreation(newCourse.getId());
        log.debug("Created course: {}", newCourse.getId());
        return newCourse;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Course edit(Course course, String editorsEmail) {
        Course oldCourse = courseRepository.getOne(course.getId());

        if (!oldCourse.getCreator().getEmail().equals(editorsEmail)) {
            log.warn("Creator of this course {} is not the editor: {}!", course, editorsEmail);
            throw new ForbiddenCourseEdit();
        }

        log.debug("Modified course: {}", course.getId());
        return courseRepository.save(course);
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
            course.setFiles(fileManagementClient.getAllFilesByTagId(hu.me.iit.malus.thesis.course.client.dto.Service.COURSE, courseId).getBody());
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
}
