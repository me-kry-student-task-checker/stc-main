package hu.me.iit.malus.thesis.task.service.exception;

/**
 * Exception class, it is thrown when a task could not be found
 *
 * @author Attila Szőke
 */
public class TaskNotFoundException extends Exception {

    private static final String ERROR_MSG = "This task could not be found!";

    public TaskNotFoundException() {
        super(ERROR_MSG);
    }
}
