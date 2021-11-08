package hu.me.iit.malus.thesis.task.service.exception;

import lombok.Getter;

/**
 * Exception class, which is thrown when a task's deletion must be rolled back
 *
 * @author Attila Szőke
 */
@Getter
public class TaskDeleteRollbackException extends Exception {

    private static final String ERROR_MSG = "Task (%d) deletion was rolled, reason: %s!";

    public TaskDeleteRollbackException(long taskId, String rollbackReason) {
        super(String.format(ERROR_MSG, taskId, rollbackReason));
    }
}
