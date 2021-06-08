package hu.me.iit.malus.thesis.course.controller;


import hu.me.iit.malus.thesis.course.controller.dto.CourseCreateDto;
import hu.me.iit.malus.thesis.course.controller.dto.CourseFullDetailsDto;
import hu.me.iit.malus.thesis.course.controller.dto.CourseModificationDto;
import hu.me.iit.malus.thesis.course.controller.dto.CourseOverviewDto;
import hu.me.iit.malus.thesis.course.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.security.Principal;
import java.util.Set;

/**
 * Controller endpoint of this service
 *
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
    public ResponseEntity<CourseOverviewDto> createCourse(@Valid @RequestBody CourseCreateDto dto, Principal principal) {
        return ResponseEntity.ok(service.create(dto, principal.getName()));
    }

    @PutMapping("/edit")
    @PreAuthorize("hasRole('ROLE_Teacher')")
    public ResponseEntity<CourseOverviewDto> editCourse(@Valid @RequestBody CourseModificationDto dto, Principal principal) {
        return ResponseEntity.ok(service.edit(dto, principal.getName()));
    }

    @GetMapping("/get/{courseId}")
    public ResponseEntity<CourseFullDetailsDto> get(@PathVariable @Min(1) Long courseId, Principal principal) {
        return ResponseEntity.ok(service.get(courseId, principal.getName()));
    }

    @GetMapping("/getAll")
    public ResponseEntity<Set<CourseOverviewDto>> getAll(Principal principal) {
        return ResponseEntity.ok(service.getAll(principal.getName()));
    }

    @DeleteMapping("/delete/{courseId}")
    @PreAuthorize("hasRole('ROLE_Teacher')")
    public void deleteCourse(@PathVariable Long courseId) {
        service.deleteCourse(courseId);
    }
}
