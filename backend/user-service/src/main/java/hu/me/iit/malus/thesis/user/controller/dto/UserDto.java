package hu.me.iit.malus.thesis.user.controller.dto;

import hu.me.iit.malus.thesis.user.model.UserRole;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public abstract class UserDto {

    @NotNull
    @Email
    private String email;

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    @NotNull
    private UserRole role;
}
