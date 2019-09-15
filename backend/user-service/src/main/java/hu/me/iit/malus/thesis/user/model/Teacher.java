package hu.me.iit.malus.thesis.user.model;

import lombok.*;

import java.util.List;

/**
 * Representation of Teacher type User
 *
 * @author Javorek Dénes
 */
@Getter @Setter
@ToString @EqualsAndHashCode
public class Teacher extends User {

    private List<Long> createdCourseIds;

    public Teacher(String email, String password, String firstName, String lastName, List<Long> createdCourseIds) {
        super(email, password, firstName, lastName, UserRole.TEACHER, false);
        this.createdCourseIds = createdCourseIds;
    }
}
