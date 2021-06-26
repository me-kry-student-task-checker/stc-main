package hu.me.iit.malus.thesis.user.service.exception;

/**
 * Exception that is thrown when a user tries to register again with an already used email address.
 *
 * @author Javorek Dénes
 */
public class UserAlreadyExistException extends Exception {

    private static final String MSG = "User with the same identifier is already registered";

    public UserAlreadyExistException() {
        super(MSG);
    }

    public UserAlreadyExistException(Throwable cause) {
        super(MSG, cause);
    }
}
