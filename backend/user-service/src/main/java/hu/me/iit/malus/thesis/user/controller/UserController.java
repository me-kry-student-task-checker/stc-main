package hu.me.iit.malus.thesis.user.controller;

import hu.me.iit.malus.thesis.user.controller.dto.*;
import hu.me.iit.malus.thesis.user.event.RegistrationCompletedEvent;
import hu.me.iit.malus.thesis.user.model.User;
import hu.me.iit.malus.thesis.user.model.exception.UserAlreadyExistException;
import hu.me.iit.malus.thesis.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.security.Principal;
import java.util.Set;

@RestController
@RequestMapping("/api/user")
@Slf4j
@RequiredArgsConstructor
public class UserController {

    private final UserService service;
    private final ApplicationEventPublisher eventPublisher;

    @PostMapping("/registration")
    public RegistrationResponse registerUserAccount(
            @RequestBody @Valid RegistrationRequest registrationRequest) {
        log.info("Registering user account by request: {}", registrationRequest);

        User registeredUser = service.registerNewUserAccount(registrationRequest);
        if (registeredUser == null) {
            throw new UserAlreadyExistException();
        }

        eventPublisher.publishEvent(new RegistrationCompletedEvent(registeredUser));
        return new RegistrationResponse("Success");
    }

    @GetMapping("/confirmation")
    public ResponseEntity<String> confirmRegistration(@RequestParam("token") @NotBlank String token) {
        boolean valid = service.activateUser(token);
        if (!valid) {
            return ResponseEntity
                    .status(HttpStatus.NOT_ACCEPTABLE)
                    .body("Account cannot be activated, invalid, expired or used token!");
        }
        return ResponseEntity
                .status(HttpStatus.OK)
                .body("Account activated. You can login now.");
    }

    @PreAuthorize("hasRole('ROLE_Teacher')")
    @PostMapping("/saveCourseCreation")
    public void saveCourseCreation(Principal principal, @RequestBody @Min(1) Long courseId) {
        service.saveCourseCreation(principal.getName(), courseId);
    }

    @PreAuthorize("hasRole('ROLE_TEACHER')")
    @PostMapping("/assignStudentsToCourse")
    public void assignStudentsToCourse(@RequestBody @Valid CourseAssignmentDto dto) {
        service.assignStudentsToCourse(dto.getCourseId(), dto.getStudentEmails());
    }

    @GetMapping("/students")
    public Set<@Valid StudentDto> getAllStudents() {
        return service.getAllStudents();
    }

    @GetMapping("/teachers")
    public Set<@Valid TeacherDto> getAllTeachers() {
        return service.getAllTeachers();
    }

    @GetMapping("/student/{email:.+}")
    public @Valid StudentDto getStudentByEmail(@PathVariable("email") @NotBlank String studentEmail) {
        return service.getStudentByEmail(studentEmail);
    }

    @GetMapping("/teacher/{email:.+}")
    public @Valid TeacherDto getTeacherByEmail(@PathVariable("email") @NotBlank String teacherEmail) {
        return service.getTeacherByEmail(teacherEmail);
    }

    @GetMapping("/student/assigned/{courseId}")
    public Set<@Valid StudentDto> getStudentsByAssignedCourseId(@PathVariable("courseId") @Min(1) Long courseId) {
        return service.getStudentsByAssignedCourseId(courseId);
    }

    @GetMapping("/student/notassigned/{courseId}")
    public Set<@Valid StudentDto> getStudentsByNotAssignedCourseId(@PathVariable("courseId") @Min(1) Long courseId) {
        return service.getStudentsByNotAssignedCourseId(courseId);
    }

    @GetMapping("/isRelated/course/{courseId}")
    public Boolean isRelated(Principal principal, @PathVariable("courseId") @Min(1) Long courseId) {
        return service.isRelatedToCourse(principal.getName(), courseId);
    }

    @GetMapping("/related/course")
    public Set<Long> getRelatedCourseIds(Principal principal) {
        return service.getRelatedCourseIds(principal.getName());
    }


    @GetMapping("/teacher/created/{courseId}")
    public @Valid TeacherDto getTeacherByCreatedCourseId(@PathVariable("courseId") @Min(1) Long courseId) {
        return service.getTeacherByCreatedCourseId(courseId);
    }

    @GetMapping("/me")
    public UserDto getMe(Principal principal) {
        return service.getDtoFromAnyUser(service.getAnyUserByEmail(principal.getName()));
    }

}
