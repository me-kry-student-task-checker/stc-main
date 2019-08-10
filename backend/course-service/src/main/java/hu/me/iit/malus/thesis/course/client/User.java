package hu.me.iit.malus.thesis.course.client;

import lombok.*;

@Getter @Setter @NoArgsConstructor
@ToString @EqualsAndHashCode
/**
 * Aggregator class for Student and Teacher objects.
 * It is not a real DTO, so it should not be used in inter-service communication,
 * use one of its child class instead.
 * @author Javorek Dénes
 */
public class User
{
    private String firstName;
    private String lastName;
    private String emailAddress;

    public User(String firstName, String lastName, String emailAddress) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.emailAddress = emailAddress;
    }
}
