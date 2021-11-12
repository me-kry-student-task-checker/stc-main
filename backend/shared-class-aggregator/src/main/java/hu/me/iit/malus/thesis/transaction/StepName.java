package hu.me.iit.malus.thesis.transaction;

public enum StepName {
    // Shared
    START_TRANSACTION,
    // Task specific
    TASK_REMOVAL, TASK_FILE_REMOVAL, TASK_COMMENT_REMOVAL, TASK_COMMENT_FILE_REMOVAL,
    // Course specific
    COURSE_COMMENT_REMOVAL, COURSE_COMMENT_FILE_REMOVAL, COURSE_FILE_REMOVAL
}
