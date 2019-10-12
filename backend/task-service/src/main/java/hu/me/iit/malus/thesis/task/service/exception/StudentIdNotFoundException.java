package hu.me.iit.malus.thesis.task.service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception class, which is thrown when a student id can not be found in a list
 *
 * @author Attila Szőke
 */
@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Student cannot be found")
public class StudentIdNotFoundException extends RuntimeException {

    private static final String ERROR_MESSAGE = "Student with this id can not be found!";

    public StudentIdNotFoundException() {
        super(ERROR_MESSAGE);
    }

    public StudentIdNotFoundException(String message) {
        super(message);
    }

    public StudentIdNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public StudentIdNotFoundException(Throwable cause) {
        super(cause);
    }

    public StudentIdNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
