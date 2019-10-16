package hu.me.iit.malus.thesis.course.service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Custom exception class, is thrown if an invitation can not be found in the database
 *
 * @author Attila Szőke
 */
@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "No valid invitation found")
public class InvitationNotFoundException extends Exception {

    private static final String ERROR_MESSAGE = "Invitation can not be found!";

    public InvitationNotFoundException() {
        super(ERROR_MESSAGE);
    }

    public InvitationNotFoundException(String message) {
        super(message);
    }

    public InvitationNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvitationNotFoundException(Throwable cause) {
        super(cause);
    }

    public InvitationNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
