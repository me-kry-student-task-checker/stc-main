package hu.me.iit.malus.thesis.feedback.service.exception;

public class ForbiddenCommentEditException extends Exception {

    private static final String MSG = "A comment can only be edited by it's author!";

    public ForbiddenCommentEditException() {
        super(MSG);
    }

    public ForbiddenCommentEditException(String message) {
        super(message);
    }

    public ForbiddenCommentEditException(String message, Throwable cause) {
        super(message, cause);
    }

    public ForbiddenCommentEditException(Throwable cause) {
        super(cause);
    }

    public ForbiddenCommentEditException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
