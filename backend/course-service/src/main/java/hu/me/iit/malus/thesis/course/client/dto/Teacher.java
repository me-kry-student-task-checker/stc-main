package hu.me.iit.malus.thesis.course.client.dto;

import lombok.*;

import java.util.List;

/**
 * Data Transfer Object for Teacher entity
 *
 * @author Javorek Dénes
 */
@Getter @Setter @NoArgsConstructor
@ToString @EqualsAndHashCode
public class Teacher extends User {

    private List<Long> createdCourseIds;

    public Teacher(String email, String firstName, String lastName, List<Long> createdCourseIds, boolean enabled) {
        super(email, null, firstName, lastName, UserRole.TEACHER, enabled);
        this.createdCourseIds = createdCourseIds;
    }

    public Teacher withEmail(String email) {
        return new Teacher(email, this.getFirstName(), this.getLastName(), this.getCreatedCourseIds(), this.isEnabled());
    }
}
