package hu.me.iit.malus.thesis.course.service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Custom exception class, is thrown if a course can not be found in the database
 *
 * @author Attila Szőke
 */
@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Course not found")
public class CourseNotFoundException extends RuntimeException {
    private static final String ERROR_MESSAGE = "Course can not be found!";

    public CourseNotFoundException() {
        super(ERROR_MESSAGE);
    }

    public CourseNotFoundException(String message) {
        super(message);
    }

    public CourseNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public CourseNotFoundException(Throwable cause) {
        super(cause);
    }

    public CourseNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
