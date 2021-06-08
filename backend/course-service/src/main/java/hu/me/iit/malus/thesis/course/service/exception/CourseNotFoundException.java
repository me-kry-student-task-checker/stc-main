package hu.me.iit.malus.thesis.course.service.exception;

/**
 * Custom exception class, is thrown if a course can not be found in the database
 *
 * @author Attila Szőke
 */
public class CourseNotFoundException extends Exception {

    private static final String ERROR_MSG = "This course could not be found!";

    public CourseNotFoundException() {
        super(ERROR_MSG);
    }
}
