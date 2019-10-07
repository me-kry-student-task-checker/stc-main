package hu.me.iit.malus.thesis.user.model.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception that is thrown when a user cannot be found by querying an email address.
 * @author Javorek Dénes
 */
@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "User cannot be found.")
@Getter
public class UserNotFoundException extends RuntimeException {
    private static final String MSG = "User, cannot be found!";
    private String email;

    public UserNotFoundException() {
        super(MSG);
    }

    public UserNotFoundException(String requestedEmail) {
        super(MSG + " Email: " + requestedEmail);
        this.email = requestedEmail;
    }
}
