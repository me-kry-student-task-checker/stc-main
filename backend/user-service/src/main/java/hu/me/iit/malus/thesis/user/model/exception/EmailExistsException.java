package hu.me.iit.malus.thesis.user.model.exception;

/**
 * Thrown in scenarios when User tries to register with an already registered email address
 * @author Javorek Dénes
 */
public class EmailExistsException extends RuntimeException {
    private static final String MSG = "The given email address already registered";

    public EmailExistsException() {
        super(MSG);
    }

    public EmailExistsException(String email) {
        super(MSG + ":" + email);
    }
}