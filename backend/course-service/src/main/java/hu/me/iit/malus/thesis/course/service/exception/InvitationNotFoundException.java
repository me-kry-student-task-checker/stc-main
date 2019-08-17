package hu.me.iit.malus.thesis.course.service.exception;

/**
 * Custom exception class, is thrown if an invitation can not be found in the database
 *
 * @author Attila Szőke
 */
public class InvitationNotFoundException extends Exception {

    private static final String errorMessage = "Invitation can not be found!";

    public InvitationNotFoundException() {
        super(errorMessage);
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
