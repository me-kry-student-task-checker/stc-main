package hu.me.iit.malus.thesis.user.controller.dto;

import hu.me.iit.malus.thesis.user.controller.validation.MatchingPasswords;
import hu.me.iit.malus.thesis.user.controller.validation.ValidPassword;
import hu.me.iit.malus.thesis.user.controller.validation.ValidRole;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@MatchingPasswords(message = "registration.invalid.passwordConfirm")
public class RegistrationRequest {

    @NotNull (message = "registration.required.email")
    @Email (message = "registration.invalid.email")
    private String email;

    @NotNull (message = "registration.required.firstName")
    @Size(min = 2, message = "registration.minLength.firstName")
    private String firstName;

    @NotNull (message = "registration.required.lastName")
    @Size(min = 2, message = "registration.minLength.lastName")
    private String lastName;

    @NotNull (message = "registration.required.role")
    @ValidRole (message = "registration.invalid.role")
    private String role;

    @NotNull (message = "registration.required.password")
    @ValidPassword (message = "registration.invalid.password")
    private String password;
    private String passwordConfirm;
}
