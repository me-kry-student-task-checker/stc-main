package hu.me.iit.malus.thesis.feedback.service.exception;

public class CommentNotFoundException extends Exception {

    private static final String MSG = "This comment could not be found!";

    public CommentNotFoundException() {
        super(MSG);
    }

    public CommentNotFoundException(String message) {
        super(message);
    }

    public CommentNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public CommentNotFoundException(Throwable cause) {
        super(cause);
    }

    public CommentNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
