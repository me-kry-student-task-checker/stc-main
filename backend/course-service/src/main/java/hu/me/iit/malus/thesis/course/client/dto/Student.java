package hu.me.iit.malus.thesis.course.client.dto;

import lombok.*;

/**
 * Data Transfer Object for Student entity
 * @author Javorek Dénes
 */
@Getter @Setter @NoArgsConstructor
@ToString @EqualsAndHashCode
public class Student extends User {

    public Student(String firstName, String lastName, String emailAddress) {
        super(firstName, lastName, emailAddress);
    }
}
