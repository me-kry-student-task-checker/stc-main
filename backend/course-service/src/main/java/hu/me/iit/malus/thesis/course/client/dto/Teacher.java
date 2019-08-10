package hu.me.iit.malus.thesis.course.client.dto;

import hu.me.iit.malus.thesis.course.client.User;
import lombok.*;

/**
 * Data Transfer Object for Teacher entity
 * @author Javorek Dénes
 */
@Getter @Setter @NoArgsConstructor
@ToString @EqualsAndHashCode
public class Teacher extends User {

    public Teacher(String firstName, String lastName, String emailAddress) {
        super(firstName, lastName, emailAddress);
    }
}
