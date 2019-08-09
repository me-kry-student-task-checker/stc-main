package hu.me.iit.malus.thesis.course.service.impl;

import hu.me.iit.malus.thesis.course.repository.CourseRepository;
import hu.me.iit.malus.thesis.course.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Default implementation for Course service.
 * @author Javorek Dénes
 */
@Service
public class CourseServiceImpl implements CourseService {
    private CourseRepository repository;

    @Autowired
    public CourseServiceImpl(CourseRepository repository) {
        this.repository = repository;
    }
}
