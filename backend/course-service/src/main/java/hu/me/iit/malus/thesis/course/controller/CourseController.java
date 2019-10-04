package hu.me.iit.malus.thesis.course.controller;


import hu.me.iit.malus.thesis.course.controller.dto.CourseDto;
import hu.me.iit.malus.thesis.course.model.Course;
import hu.me.iit.malus.thesis.course.service.CourseService;
import hu.me.iit.malus.thesis.course.service.exception.CourseNotFoundException;
import hu.me.iit.malus.thesis.course.service.exception.InvitationNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

/**
 * Controller endpoint of this service
 * @author Javorek Dénes
 * @author Attila Szőke
 */
@RestController
@RequestMapping("/api/course")
public class CourseController {

    private CourseService service;
    private Principal principal;

    @Autowired
    public CourseController(CourseService service) {
        this.service = service;
    }

    @PostMapping("/create")
    public Course createCourse(@RequestBody CourseDto course) {
        course.setCreator(principal.getName());
        return service.create(course);
    }

    @PostMapping("/edit")
    public Course editCourse(@RequestBody CourseDto course) {
        course.setCreator(principal.getName());
        return service.edit(course);
    }

    @GetMapping("/get/{courseId}")
    //TODO exception handling with controller advice?
    public Course get(@PathVariable Long courseId) throws CourseNotFoundException {
        return service.get(courseId);
    }

    @GetMapping("/getAll")
    public Iterable<Course> getAll() {
        return service.getAll();
    }

    @PostMapping("/invite/{courseId}/{studentId}")
    public void invite(@PathVariable Long courseId, @PathVariable String studentId) {
        service.invite(courseId, studentId);
    }

    @PostMapping("/invite/{courseId}")
    public void invite(@PathVariable Long courseId, @RequestBody List<String> studentIds) {
        service.invite(courseId, studentIds);
    }

    @PostMapping("/acceptInvitation/{invitationUuid}")
    //TODO exception handling with controller advice?
    public void acceptInvite(@PathVariable String invitationUuid) throws InvitationNotFoundException {
        service.acceptInvite(invitationUuid);
    }

}
