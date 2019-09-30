package hu.me.iit.malus.thesis.user.controller;

import hu.me.iit.malus.thesis.user.controller.dto.RegistrationRequest;
import hu.me.iit.malus.thesis.user.controller.dto.RegistrationResponse;
import hu.me.iit.malus.thesis.user.event.RegistrationCompletedEvent;
import hu.me.iit.malus.thesis.user.model.Student;
import hu.me.iit.malus.thesis.user.model.Teacher;
import hu.me.iit.malus.thesis.user.model.exception.UserAlreadyExistException;
import hu.me.iit.malus.thesis.user.model.User;
import hu.me.iit.malus.thesis.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.Set;

@RestController
@RequestMapping("/api/user")
@Slf4j
public class UserController {
    private UserService service;
    private ApplicationEventPublisher eventPublisher;

    @Autowired
    public UserController(UserService service, ApplicationEventPublisher eventPublisher) {
        this.service = service;
        this.eventPublisher = eventPublisher;
    }

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
    public ResponseEntity<String> confirmRegistration(@RequestParam("token") String token) {
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

    @PostMapping("/saveStudent")
    public void saveStudent(@RequestBody Student student) {
        service.saveStudent(student);
    }

    @PostMapping("/saveStudents")
    public void saveStudents(@RequestBody Set<Student> studentsToSave) {
        service.saveStudents(studentsToSave);
    }

    @PostMapping("/saveTeacher")
    public void saveTeacher(@RequestBody Teacher teacher) {
        service.saveTeacher(teacher);
    }

    @PostMapping("/saveTeachers")
    public void saveTeachers(@RequestBody Set<Teacher> teachersToSave) {
        service.saveTeachers(teachersToSave);
    }

    @GetMapping("/students")
    public Set<Student> getAllStudents() {
        return service.getAllStudents();
    }

    @GetMapping("/teachers")
    public Set<Teacher> getAllTeachers() {
        return service.getAllTeachers();
    }

    @GetMapping("/student/{email:.+}")
    public Student getStudentByEmail(@PathVariable("email") String studentEmail) {
        return service.getStudentByEmail(studentEmail);
    }

    @GetMapping("/teacher/{email:.+}")
    public Teacher getTeacherByEmail(@PathVariable("email") String teacherEmail) {
        return service.getTeacherByEmail(teacherEmail);
    }

    @GetMapping("/{email:.+}")
    public User getUserByEmail(@PathVariable("email") String userEmail) {
        return service.getAnyUserByEmail(userEmail);
    }

    @GetMapping("/me")
    public User getMe(Principal principal) {
        return service.getAnyUserByEmail(principal.getName());
    }
}
