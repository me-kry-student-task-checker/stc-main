package hu.me.iit.malus.thesis.course.model.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Thrown when a user (Teacher) tries to edit a Course, which is not owned by him.
 * @author Javorek Dénes
 */
@ResponseStatus(value = HttpStatus.FORBIDDEN, reason = "The course cannot be edited by this user")
public class ForbiddenCourseEdit extends RuntimeException {
    private static final String MSG = "Course edit is not permitted for this user.";

    public ForbiddenCourseEdit() {
        super(MSG);
    }

    public ForbiddenCourseEdit(Throwable cause) {
        super(MSG, cause);
    }
}
