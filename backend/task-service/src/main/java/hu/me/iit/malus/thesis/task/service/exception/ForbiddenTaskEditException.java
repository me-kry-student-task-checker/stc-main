package hu.me.iit.malus.thesis.task.service.exception;

/**
 * Exception class, it is thrown when a task could not be found
 *
 * @author Attila Szőke
 */
public class ForbiddenTaskEditException extends Exception {

    private static final String ERROR_MSG = "This task cannot be edited by this user!";

    public ForbiddenTaskEditException() {
        super(ERROR_MSG);
    }
}
