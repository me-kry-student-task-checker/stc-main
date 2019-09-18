package hu.me.iit.malus.thesis.user.model;

import lombok.*;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import java.util.List;

/**
 * Representation of Teacher type User
 * @author Javorek Dénes
 */
@Entity
@Getter @Setter
@ToString @EqualsAndHashCode
@NoArgsConstructor
public class Teacher extends User {

    @ElementCollection
    private List<Long> createdCourseIds;

    public Teacher(String email, String password, String firstName, String lastName, List<Long> createdCourseIds) {
        super(email, password, firstName, lastName, UserRole.TEACHER, false);
        this.createdCourseIds = createdCourseIds;
    }
}
