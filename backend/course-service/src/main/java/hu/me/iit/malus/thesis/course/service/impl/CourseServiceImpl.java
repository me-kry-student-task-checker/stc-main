package hu.me.iit.malus.thesis.course.service.impl;

import hu.me.iit.malus.thesis.course.client.*;
import hu.me.iit.malus.thesis.course.model.Course;
import hu.me.iit.malus.thesis.course.model.exception.ForbiddenCourseEdit;
import hu.me.iit.malus.thesis.course.repository.CourseRepository;
import hu.me.iit.malus.thesis.course.service.CourseService;
import hu.me.iit.malus.thesis.course.service.exception.CourseNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
public class CourseServiceImpl implements CourseService {

    private CourseRepository courseRepository;
    private TaskClient taskClient;
    private FeedbackClient feedbackClient;
    private UserClient userClient;
    private FileManagementClient fileManagementClient;
    private EmailClient emailClient;

    /**
     * Instantiates a new Course service.
     *
     * @param courseRepository     the course repository
     * @param taskClient           the task client
     * @param feedbackClient       the feedback client
     * @param userClient           the user client
     * @param fileManagementClient the fileManagement client
     * @param emailClient          the email client
     */
    @Autowired
    public CourseServiceImpl(CourseRepository courseRepository, TaskClient taskClient, FeedbackClient feedbackClient, UserClient userClient,
                             FileManagementClient fileManagementClient, EmailClient emailClient) {
        this.courseRepository = courseRepository;
        this.taskClient = taskClient;
        this.feedbackClient = feedbackClient;
        this.userClient = userClient;
        this.fileManagementClient = fileManagementClient;
        this.emailClient = emailClient;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Course create(Course course, String creatorsEmail) {
        course.setCreationDate(Date.from(Instant.now()));
        Course newCourse = courseRepository.save(course);
        userClient.saveCourseCreation(newCourse.getId());
        log.info("Created course: {}", newCourse.getId());
        return newCourse;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Course edit(Course course, String editorsEmail) {
        Course oldCourse = courseRepository.getOne(course.getId());

        if (!oldCourse.getCreator().getEmail().equals(editorsEmail)) {
            throw new ForbiddenCourseEdit();
        }

        log.info("Modified course: {}", course.getId());
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
                throw new CourseNotFoundException();
            }

            //TODO: We should find a way to fire these as async requests
            course.setCreator(userClient.getTeacherByCreatedCourseId(courseId));
            course.setStudents(userClient.getStudentsByAssignedCourseId(courseId));
            course.setTasks(taskClient.getAllTasks(courseId));
            course.setFiles(fileManagementClient.getAllFilesByTagId(hu.me.iit.malus.thesis.course.client.dto.Service.COURSE, courseId).getBody());
            course.setComments(feedbackClient.getAllCourseComments(courseId));
            log.info("Course found: {}", courseId);
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
        log.info("Get all courses done, total number of courses is {}", relatedCourses.size());
        return relatedCourses;
    }
}
