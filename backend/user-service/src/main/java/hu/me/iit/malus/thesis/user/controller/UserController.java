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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
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
            @Valid RegistrationRequest registrationRequest, HttpServletRequest httpRequest) {
        User registeredUser = service.registerNewUserAccount(registrationRequest);
        if (registeredUser == null) {
            throw new UserAlreadyExistException();
        }
        String appUrl = "http://" + httpRequest.getServerName() + ":" + httpRequest.getServerPort();

        eventPublisher.publishEvent(
                new RegistrationCompletedEvent(registeredUser, httpRequest.getLocale(), appUrl));

        return new RegistrationResponse("!!");
    }
}
