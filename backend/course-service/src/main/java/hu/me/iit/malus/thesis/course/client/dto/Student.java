package hu.me.iit.malus.thesis.course.client.dto;

import lombok.*;

/**
 * Data Transfer Object for Student entity
 *
 * @author Javorek Dénes
 */
@Getter @Setter @NoArgsConstructor
@ToString @EqualsAndHashCode
public class Student extends User {

    public Student(String id, String firstName, String lastName) {
        super(id, firstName, lastName);
    }
}
