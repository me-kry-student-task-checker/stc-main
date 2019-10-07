package hu.me.iit.malus.thesis.course.controller;


import hu.me.iit.malus.thesis.course.client.dto.Teacher;
import hu.me.iit.malus.thesis.course.client.dto.User;
import hu.me.iit.malus.thesis.course.controller.converters.DtoConverter;
import hu.me.iit.malus.thesis.course.controller.dto.CourseDto;
import hu.me.iit.malus.thesis.course.model.Course;
import hu.me.iit.malus.thesis.course.service.CourseService;
import hu.me.iit.malus.thesis.course.service.exception.CourseNotFoundException;
import hu.me.iit.malus.thesis.course.service.exception.InvitationNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Collection;
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

    @Autowired
    public CourseController(CourseService service) {
        this.service = service;
    }

    @PostMapping("/create")
    @PreAuthorize("hasRole('ROLE_Teacher')")
    public Course createCourse(@RequestBody CourseDto courseDto, Principal principal) {
        return service.create(DtoConverter.CourseDtoToCourse(courseDto), principal.getName());
    }

    @PostMapping("/edit")
    @PreAuthorize("hasRole('ROLE_Teacher')")
    public Course editCourse(@RequestBody CourseDto courseDto, Principal principal) {
        return service.edit(DtoConverter.CourseDtoToCourse(courseDto), principal.getName());
    }

    @GetMapping("/get/{courseId}")
    public Course get(@PathVariable Long courseId, Principal principal) {
        return service.get(courseId, principal.getName());
    }

    @GetMapping("/getAll")
    public List<Course> getAll(Principal principal) {
        return service.getAll(principal.getName());
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
    public void acceptInvite(@PathVariable String invitationUuid) throws InvitationNotFoundException {
        service.acceptInvite(invitationUuid);
    }

}
