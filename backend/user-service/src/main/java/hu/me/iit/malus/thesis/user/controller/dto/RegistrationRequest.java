package hu.me.iit.malus.thesis.user.controller.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class RegistrationRequest {
    private String email;
    private String firstName;
    private String lastName;
    private String role;
    private String password;
    private String passwordConfirm;
}
