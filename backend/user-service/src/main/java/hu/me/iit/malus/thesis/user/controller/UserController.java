package hu.me.iit.malus.thesis.user.controller;

import hu.me.iit.malus.thesis.user.controller.dto.RegistrationRequest;
import hu.me.iit.malus.thesis.user.controller.dto.RegistrationResponse;
import hu.me.iit.malus.thesis.user.event.RegistrationCompletedEvent;
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

@RestController
@RequestMapping("/api/user")
@Slf4j
public class UserController {
    UserService service;
    ApplicationEventPublisher eventPublisher;

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
                .body("Account activated");
    }
}
