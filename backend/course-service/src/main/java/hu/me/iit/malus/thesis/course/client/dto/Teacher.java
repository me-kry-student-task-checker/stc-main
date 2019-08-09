package hu.me.iit.malus.thesis.course.client.dto;

import lombok.*;

/**
 * Data Transfer Object for Teacher entity
 * @author Javorek Dénes
 */
@Getter @Setter @NoArgsConstructor
@ToString @EqualsAndHashCode
public class Teacher {
    private Long id;
    private String firstName;
    private String lastName;
    private String title;

    public Teacher(String firstName, String lastName, String title) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.title = title;
    }
}
