package hu.me.iit.malus.thesis.course.service.exception;

/**
 * Thrown when a user (Teacher) tries to edit a Course, which is not owned by him.
 *
 * @author Javorek Dénes
 * @author Attila Szőke
 */
public class ForbiddenCourseEditException extends Exception {

    private static final String ERROR_MSG = "Course edit is not permitted for this user!";

    public ForbiddenCourseEditException() {
        super(ERROR_MSG);
    }
}
