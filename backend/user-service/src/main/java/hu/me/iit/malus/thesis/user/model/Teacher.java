package hu.me.iit.malus.thesis.user.model;

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

    public Teacher(String id, String firstName, String lastName, List<Long> createdCourseIds) {
        super(id, firstName, lastName);
        this.createdCourseIds = createdCourseIds;
    }
}
