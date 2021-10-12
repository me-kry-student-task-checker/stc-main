package hu.me.iit.malus.thesis.user.service.exception;

public class CannotReadLoginRequestException extends RuntimeException {
    private static final String MSG = "Can not read login request.";

    public CannotReadLoginRequestException(Throwable cause) {
        super(MSG, cause);
    }
}
