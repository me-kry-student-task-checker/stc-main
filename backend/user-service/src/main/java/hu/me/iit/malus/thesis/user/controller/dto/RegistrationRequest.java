package hu.me.iit.malus.thesis.user.controller.dto;

import hu.me.iit.malus.thesis.user.controller.validation.MatchingPasswords;
import hu.me.iit.malus.thesis.user.controller.validation.ValidPassword;
import hu.me.iit.malus.thesis.user.controller.validation.ValidRole;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter @Setter
@MatchingPasswords (message = "registration.invalid.passwordConfirm")
public class RegistrationRequest {
    @Email (message = "{registration.invalid.email}")
    private String email;

    @NotNull (message = "{registration.required.firstName")
    @Size(min = 2, message = "{registration.minLength.firstName}")
    private String firstName;

    @NotNull (message = "{registration.required.lastName}")
    @Size(min = 2, message = "{registration.minLength.lastName}")
    private String lastName;

    @ValidRole (message = "{registration.invalid.role}")
    private String role;

    @ValidPassword (message = "{registration.invalid.password}")
    private String password;
    private String passwordConfirm;
}
