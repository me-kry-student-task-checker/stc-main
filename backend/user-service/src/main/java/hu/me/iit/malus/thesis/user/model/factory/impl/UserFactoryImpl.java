package hu.me.iit.malus.thesis.user.model.factory.impl;

import hu.me.iit.malus.thesis.user.controller.dto.RegistrationRequest;
import hu.me.iit.malus.thesis.user.model.*;
import hu.me.iit.malus.thesis.user.model.factory.UserFactory;
import hu.me.iit.malus.thesis.user.repository.AdminRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
@RequiredArgsConstructor
public class UserFactoryImpl implements UserFactory {

    private final BCryptPasswordEncoder passwordEncoder;
    private final AdminRepository adminRepository;

    @Override
    public User create(RegistrationRequest registrationRequest) {
        var userRole = UserRole.fromString(registrationRequest.getRole());
        switch (userRole) {
            case ADMIN:
                return new Admin(
                        registrationRequest.getEmail(), passwordEncoder.encode(registrationRequest.getPassword()),
                        registrationRequest.getFirstName(), registrationRequest.getLastName());
            case TEACHER:
                return new Teacher(
                        registrationRequest.getEmail(), passwordEncoder.encode(registrationRequest.getPassword()),
                        registrationRequest.getFirstName(), registrationRequest.getLastName(), Collections.emptyList());
            case STUDENT:
                return new Student(
                        registrationRequest.getEmail(), passwordEncoder.encode(registrationRequest.getPassword()),
                        registrationRequest.getFirstName(), registrationRequest.getLastName(), Collections.emptyList());
        }
        throw new IllegalStateException("User type cannot be recognized");
    }
}
