package hu.me.iit.malus.thesis.user.model;


import lombok.*;

import java.util.List;

/**
 * Representation of Student type User
 *
 * @author Javorek Dénes
 */
@Getter @Setter
@ToString @EqualsAndHashCode
public class Student extends User {

    private List<Long> assignedCourseIds;

    public Student(String id, String password, String firstName, String lastName, List<Long> assignedCourseIds) {
        super(id, password, firstName, lastName, UserRole.STUDENT, false);
        this.assignedCourseIds = assignedCourseIds;
    }
}
