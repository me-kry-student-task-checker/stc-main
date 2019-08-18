package hu.me.iit.malus.thesis.course.service.exception;

/**
 * Custom exception class, is thrown if a course can not be found in the database
 *
 * @author Attila Szőke
 */
public class CourseNotFoundException extends Exception {

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
