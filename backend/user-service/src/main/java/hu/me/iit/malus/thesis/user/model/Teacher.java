package hu.me.iit.malus.thesis.user.model;

import lombok.*;

import java.util.List;

/**
 * Representation of a Teacher type User
 *
 * @author Javorek Dénes
 */
@Getter @Setter @NoArgsConstructor
@ToString @EqualsAndHashCode
public class Teacher extends User {

    private List<Long> createdCourseIds;

    public Teacher(String id, String password, String firstName, String lastName, List<Long> createdCourseIds) {
        super(id, password, firstName, lastName, UserRole.TEACHER);
        this.createdCourseIds = createdCourseIds;
    }
}
