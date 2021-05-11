package hu.me.iit.malus.thesis.course.controller;


import hu.me.iit.malus.thesis.course.controller.converters.DtoConverter;
import hu.me.iit.malus.thesis.course.controller.dto.CourseModificationDto;
import hu.me.iit.malus.thesis.course.controller.dto.CourseOverviewDto;
import hu.me.iit.malus.thesis.course.model.Course;
import hu.me.iit.malus.thesis.course.service.CourseService;
import hu.me.iit.malus.thesis.course.service.exception.CourseNotFoundException;
import hu.me.iit.malus.thesis.course.service.exception.ForbiddenCourseEditException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.security.Principal;
import java.util.Set;

/**
 * Controller endpoint of this service
 * @author Javorek Dénes
 * @author Attila Szőke
 */
@RestController
@RequestMapping("/api/course")
@RequiredArgsConstructor
public class CourseController {

    private final CourseService service;

    @PostMapping("/create")
    @PreAuthorize("hasRole('ROLE_Teacher')")
    public Course createCourse(@Valid @RequestBody CourseModificationDto courseModificationDto, Principal principal) {
        return service.create(DtoConverter.CourseDtoToCourse(courseModificationDto), principal.getName());
    }

    @PostMapping("/edit")
    @PreAuthorize("hasRole('ROLE_Teacher')")
    public Course editCourse(@Valid @RequestBody CourseModificationDto courseModificationDto, Principal principal) throws ForbiddenCourseEditException {
        return service.edit(DtoConverter.CourseDtoToCourse(courseModificationDto), principal.getName());
    }

    @GetMapping("/get/{courseId}")
    public Course get(@PathVariable @Min(1) Long courseId, Principal principal) throws CourseNotFoundException {
        return service.get(courseId, principal.getName());
    }

    @GetMapping("/getAll")
    public Set<CourseOverviewDto> getAll(Principal principal) {
        return DtoConverter.CourseToCourseOverviewSet(service.getAll(principal.getName()));
    }
}
