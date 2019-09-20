package hu.me.iit.malus.thesis.user.model;


import lombok.*;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import java.util.List;

/**
 * Representation of Student type User
 * @author Javorek Dénes
 */
@Entity
@Getter @Setter
@ToString
@NoArgsConstructor
public class Student extends User {

    @ElementCollection
    private List<Long> assignedCourseIds;

    public Student(String email, String password, String firstName, String lastName, List<Long> assignedCourseIds) {
        super(email, password, firstName, lastName, UserRole.STUDENT, false);
        this.assignedCourseIds = assignedCourseIds;
    }
}
