package hu.me.iit.malus.thesis.course.controller;


import hu.me.iit.malus.thesis.course.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller endpoint of this service
 * @author Javorek Dénes
 */
@RestController("/api/course")
public class CourseController {
    private CourseService service;

    @Autowired
    public CourseController(CourseService service) {
        this.service = service;
    }
}
