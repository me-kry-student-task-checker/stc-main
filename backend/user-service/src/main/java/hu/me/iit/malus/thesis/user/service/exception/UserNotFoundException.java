package hu.me.iit.malus.thesis.user.service.exception;

import lombok.Getter;

/**
 * Exception that is thrown when a user cannot be found by querying an email address.
 *
 * @author Javorek Dénes
 */
@Getter
public class UserNotFoundException extends Exception {

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
