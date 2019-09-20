package hu.me.iit.malus.thesis.user.controller.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
public class LoginRequest {
    private String email;
    private String password;
}
