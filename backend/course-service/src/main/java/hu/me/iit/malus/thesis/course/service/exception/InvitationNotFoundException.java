package hu.me.iit.malus.thesis.course.service.exception;

public class InvitationNotFoundException extends Exception {

    public InvitationNotFoundException() {
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
