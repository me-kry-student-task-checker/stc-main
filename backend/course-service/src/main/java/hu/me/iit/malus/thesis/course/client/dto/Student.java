package hu.me.iit.malus.thesis.course.client.dto;

import lombok.*;

/**
 * Data Transfer Object for Student entity
 * @author Javorek Dénes
 */
@Getter @Setter @NoArgsConstructor
@ToString @EqualsAndHashCode
public class Student {
    private Long id;
    private String firstName;
    private String lastName;
    private String emailAddress;

    public Student(String firstName, String lastName, String emailAddress) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.emailAddress = emailAddress;
    }
}
