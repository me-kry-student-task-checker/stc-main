package hu.me.iit.malus.thesis.user.model;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;

@Entity
@Getter @Setter @NoArgsConstructor
@ToString @EqualsAndHashCode
/**
 * Aggregator class for Student and Teacher objects.
 * It is not a real DTO, so it should not be used in inter-service communication,
 * use one of its child class instead.
 *
 * @author Javorek Dénes
 */
public class User
{
    @Id
    private String email;
    private String password;
    private String firstName;
    private String lastName;

    @Enumerated(EnumType.STRING)
    private UserRole role;

    User(String email, String password, String firstName, String lastName, UserRole role) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.role = role;
    }
}
