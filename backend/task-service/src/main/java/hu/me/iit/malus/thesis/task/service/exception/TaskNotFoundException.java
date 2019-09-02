package hu.me.iit.malus.thesis.task.service.exception;

/**
 * Exception class, it is thrown when a task could not be found
 *
 * @author Attila Szőke
 */
public class TaskNotFoundException extends Exception {

    private static final String ERORR_MESSAGE = "Task can not be found!";

    public TaskNotFoundException() {
        super(ERORR_MESSAGE);
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
