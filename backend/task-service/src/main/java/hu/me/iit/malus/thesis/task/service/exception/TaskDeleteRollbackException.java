package hu.me.iit.malus.thesis.task.service.exception;

import lombok.Getter;

import java.util.Arrays;

/**
 * Exception class, which is thrown when a task's deletion must be rolled back
 *
 * @author Attila Szőke
 */
@Getter
public class TaskDeleteRollbackException extends Exception {

    private static final String ERROR_MSG = "Task (%d) deletion was rolled, reason: %s!";

    public TaskDeleteRollbackException(long taskId, Exception cause) {
        super(String.format(ERROR_MSG, taskId, Arrays.toString(cause.getStackTrace())));
    }
}
