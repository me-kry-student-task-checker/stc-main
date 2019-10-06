package hu.me.iit.malus.thesis.course.client.dto;

import lombok.*;

import java.util.List;

/**
 * Data Transfer Object for Student entity
 *
 * @author Javorek Dénes
 */
@Getter @Setter @NoArgsConstructor
@ToString @EqualsAndHashCode
public class Student extends User {

    private List<Long> assignedCourseIds;

    public Student(String email, String firstName, String lastName, List<Long> assignedCourseIds, boolean enabled) {
        super(email, null, firstName, lastName, UserRole.STUDENT, enabled);
        this.assignedCourseIds = assignedCourseIds;
    }

    public Student withEmail(String email) {
        return new Student(email, this.getFirstName(), this.getLastName(), this.getAssignedCourseIds(), this.isEnabled());
    }
}
