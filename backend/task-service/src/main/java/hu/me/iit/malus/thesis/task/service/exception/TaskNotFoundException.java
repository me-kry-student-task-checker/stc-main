package hu.me.iit.malus.thesis.task.service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception class, it is thrown when a task could not be found
 *
 * @author Attila Szőke
 */
@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Task cannot be found")
public class TaskNotFoundException extends RuntimeException {

    private static final String ERROR_MESSAGE = "Task cannot be found!";

    public TaskNotFoundException() {
        super(ERROR_MESSAGE);
    }

    public TaskNotFoundException(String message) {
        super(message);
    }

    public TaskNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public TaskNotFoundException(Throwable cause) {
        super(cause);
    }

    public TaskNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
