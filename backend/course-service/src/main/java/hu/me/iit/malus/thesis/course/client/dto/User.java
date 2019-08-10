package hu.me.iit.malus.thesis.course.client.dto;

import lombok.*;

@Getter @Setter @NoArgsConstructor
@ToString @EqualsAndHashCode
/**
 * Aggregator class for Student and Teacher objects.
 * It is not a real DTO, so it should not be used in inter-service communication,
 * use one of its child class instead.
 *
 * @author Javorek Dénes
 */ class User
{
    // the id of a user is it's e-mail address
    private String id;
    private String firstName;
    private String lastName;

    User(String id, String firstName, String lastName) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
    }
}
