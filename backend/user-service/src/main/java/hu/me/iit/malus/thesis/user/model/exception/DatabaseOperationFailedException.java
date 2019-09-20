package hu.me.iit.malus.thesis.user.model.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Thrown when some kind of database exception occurs, and the requested operation fails.
 * The concrete exception must be wrapped inside this one.
 * @author Javorek Dénes
 */
@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR, reason = "Operation failed")
public class DatabaseOperationFailedException extends RuntimeException {
    private static final String MSG = "Operation failed at database level.";

    public DatabaseOperationFailedException(Throwable cause) {
        super(MSG, cause);
    }
}
