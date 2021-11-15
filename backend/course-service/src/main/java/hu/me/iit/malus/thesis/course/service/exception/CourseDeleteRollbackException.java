package hu.me.iit.malus.thesis.course.service.exception;

import lombok.Getter;

import java.util.Arrays;

/**
 * Exception class, which is thrown when a course's deletion must be rolled back
 *
 * @author Attila Szőke
 */
@Getter
public class CourseDeleteRollbackException extends Exception {

    private static final String ERROR_MSG = "Course (%d) deletion was rolled, reason: %s!";

    public CourseDeleteRollbackException(long courseId, Exception cause) {
        super(String.format(ERROR_MSG, courseId, Arrays.toString(cause.getStackTrace())));
    }
}
