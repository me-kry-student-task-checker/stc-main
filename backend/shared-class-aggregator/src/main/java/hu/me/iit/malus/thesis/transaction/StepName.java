package hu.me.iit.malus.thesis.transaction;

/**
 * An enum class, specifying the steps of the transactions in the application.
 * Mainly used for logging purposes.
 * Can be extended if needed.
 *
 * @author Attila Szőke
 */
public enum StepName {
    // Shared
    START_TRANSACTION,
    // Task specific
    TASK_REMOVAL, TASK_FILE_REMOVAL, TASK_COMMENT_REMOVAL, TASK_COMMENT_FILE_REMOVAL,
    // Course specific
    COURSE_COMMENT_REMOVAL, COURSE_COMMENT_FILE_REMOVAL, COURSE_FILE_REMOVAL
}
