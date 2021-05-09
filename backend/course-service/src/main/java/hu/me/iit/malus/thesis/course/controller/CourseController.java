package hu.me.iit.malus.thesis.course.controller;


import hu.me.iit.malus.thesis.course.controller.dto.CourseCreateDto;
import hu.me.iit.malus.thesis.course.controller.dto.CourseFullDetailsDto;
import hu.me.iit.malus.thesis.course.controller.dto.CourseModificationDto;
import hu.me.iit.malus.thesis.course.controller.dto.CourseOverviewDto;
import hu.me.iit.malus.thesis.course.service.CourseService;
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
    public CourseOverviewDto createCourse(@Valid @RequestBody CourseCreateDto dto, Principal principal) {
        return service.create(dto, principal.getName());
    }

    @PostMapping("/edit")
    @PreAuthorize("hasRole('ROLE_Teacher')")
    public CourseOverviewDto editCourse(@Valid @RequestBody CourseModificationDto dto, Principal principal) {
        return service.edit(dto, principal.getName());
    }

    @GetMapping("/get/{courseId}")
    public CourseFullDetailsDto get(@PathVariable @Min(1) Long courseId, Principal principal) {
        return service.get(courseId, principal.getName());
    }

    @GetMapping("/getAll")
    public Set<CourseOverviewDto> getAll(Principal principal) {
        return service.getAll(principal.getName());
    }
}
