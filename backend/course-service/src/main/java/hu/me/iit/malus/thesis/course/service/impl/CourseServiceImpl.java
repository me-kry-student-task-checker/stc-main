package hu.me.iit.malus.thesis.course.service.impl;

import hu.me.iit.malus.thesis.course.client.FeedbackClient;
import hu.me.iit.malus.thesis.course.client.TaskClient;
import hu.me.iit.malus.thesis.course.client.UserClient;
import hu.me.iit.malus.thesis.course.client.dto.Student;
import hu.me.iit.malus.thesis.course.client.dto.Teacher;
import hu.me.iit.malus.thesis.course.model.Course;
import hu.me.iit.malus.thesis.course.repository.CourseRepository;
import hu.me.iit.malus.thesis.course.service.CourseService;
import hu.me.iit.malus.thesis.course.service.exception.CourseNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Default implementation for Course service.
 *
 * @author Javorek Dénes
 */
@Service
@Slf4j
public class CourseServiceImpl implements CourseService {

    private final CourseRepository repository;

    /**
     * Instantiates a new Course service.
     *
     * @param repository the repository
     */
    @Autowired
    public CourseServiceImpl(CourseRepository repository) {
        this.repository = repository;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Course create(Course course) {
        log.info("Created course: {}", course);
        UserClient.save(course.getStudents());
        UserClient.save(course.getCreator());
        TaskClient.save(course.getTasks());
        FeedbackClient.save(course.getComments());
        return repository.save(course);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Course edit(Course course) {
        log.info("Modified course: {}", course);
        UserClient.save(course.getStudents());
        UserClient.save(course.getCreator());
        TaskClient.save(course.getTasks());
        FeedbackClient.save(course.getComments());
        return repository.save(course);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Course get(Long courseId) throws CourseNotFoundException {
        Teacher creator = null;
        Set<Student> students = new HashSet<>();
        Optional<Course> opt = repository.findById(courseId);
        if (opt.isPresent()) {
            for (Teacher teacher : UserClient.getAllTeachers()) {
                for (Long createdCourseId : teacher.getCreatedCourseIds()) {
                    if (createdCourseId.equals(courseId)) {
                        creator = teacher;
                        break;
                    }
                }
            }
            for (Student student : UserClient.getAllStudents()) {
                for (Long assignedCourseId : student.getAssignedCourseIds()) {
                    if (assignedCourseId.equals(courseId)) {
                        students.add(student);
                        break;
                    }
                }
            }
            Course course = opt.get();
            course.setCreator(creator);
            course.setStudents(students);
            course.setTasks(TaskClient.getAllByCourseId(courseId));
            course.setComments(FeedbackClient.getByCourseId(courseId));
            return course;
        } else {
            throw new CourseNotFoundException();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Iterable<Course> getAll() {
        Set<Student> students;
        Iterable<Course> courses = repository.findAll();
        for (Course course : courses) {
            for (Teacher teacher : UserClient.getAllTeachers()) {
                for (Long createdCourseId : teacher.getCreatedCourseIds()) {
                    if (createdCourseId.equals(course.getId())) {
                        course.setCreator(teacher);
                    }
                }
            }
            students = new HashSet<>();
            for (Student student : UserClient.getAllStudents()) {
                for (Long assignedCourseId : student.getAssignedCourseIds()) {
                    if (assignedCourseId.equals(course.getId())) {
                        students.add(student);
                    }
                }
            }
            course.setStudents(students);
            course.setTasks(TaskClient.getAllByCourseId(course.getId()));
            course.setComments(FeedbackClient.getByCourseId(course.getId()));
        }
        return courses;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void inviteStudent(String studentId, Long courseId) {
        return;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void inviteStudents(List<String> studentIds, List<Long> courseIds) {
        return;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void acceptInvite(String inviteUUID) {

    }
}
