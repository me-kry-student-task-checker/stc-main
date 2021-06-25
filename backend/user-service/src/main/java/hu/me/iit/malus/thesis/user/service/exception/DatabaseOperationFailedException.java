package hu.me.iit.malus.thesis.user.service.exception;

/**
 * Thrown when some kind of database exception occurs, and the requested operation fails.
 * The concrete exception must be wrapped inside this one.
 *
 * @author Javorek Dénes
 */
public class DatabaseOperationFailedException extends Exception {

    private static final String MSG = "Operation failed at database level.";

    public DatabaseOperationFailedException(Throwable cause) {
        super(MSG, cause);
    }
}
