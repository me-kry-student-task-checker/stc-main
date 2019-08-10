package hu.me.iit.malus.thesis.course.client.dto;

import lombok.*;

/**
 * Data Transfer Object for Teacher entity
 *
 * @author Javorek Dénes
 */
@Getter @Setter @NoArgsConstructor
@ToString @EqualsAndHashCode
public class Teacher extends User {

    public Teacher(String id, String firstName, String lastName) {
        super(id, firstName, lastName);
    }
}
