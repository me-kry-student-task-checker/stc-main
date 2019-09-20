package hu.me.iit.malus.thesis.user.model.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Thrown when someone tries to insert a new User, where it is only acceptable to update an existing one.
 * @author Javorek Dénes
 */
@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Only existing users can be saved.")
public class IllegalUserInsertionException extends RuntimeException {
    private static final String MSG = "Tried to insert a new user, where it is not acceptable";

    public IllegalUserInsertionException() {
        super(MSG);
    }

    public IllegalUserInsertionException(Throwable cause) {
        super(MSG, cause);
    }
}
