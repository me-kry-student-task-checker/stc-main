package hu.me.iit.malus.thesis.course.service.impl;

import hu.me.iit.malus.thesis.course.model.Course;
import hu.me.iit.malus.thesis.course.repository.CourseRepository;
import hu.me.iit.malus.thesis.course.service.CourseService;
import hu.me.iit.malus.thesis.course.service.exception.CourseNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

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
        //TODO send requests to save creators, students, tasks, comments
        return repository.save(course);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Course edit(Course course) {
        //TODO send requests to modify creators, students, tasks, comments
        return repository.save(course);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Course get(Long courseId) throws CourseNotFoundException {
        Optional<Course> opt = repository.findById(courseId);
        if (opt.isPresent()) {
            Course course = opt.get();
            //TODO set values from other services
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
        Iterable<Course> courses = repository.findAll();
        //TODO set values from other services
        return courses;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String inviteStudent(String studentId, Long courseId) {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void acceptInvite(String inviteUUID) {

    }
}
