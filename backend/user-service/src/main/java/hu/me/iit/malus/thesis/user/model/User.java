package hu.me.iit.malus.thesis.user.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.persistence.*;


/**
 * Aggregator class for all possible User objects.
 * It is not a real DTO, so it should not be used in inter-service communication,
 * use one of its child class instead.
 *
 * @author Javorek Dénes
 */
@Entity
@Inheritance
@Getter @Setter
@AllArgsConstructor
@ToString @EqualsAndHashCode
public class User
{
    @Id
    private final String email;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;
    private String firstName, lastName;

    @Enumerated(EnumType.STRING)
    private UserRole role;
    private boolean enabled;

    public User() {
        super();
        this.email = null;
        this.enabled = false;
    }

    User withEmail(String email) {
        return new User(email, this.password, this.firstName, this.lastName, this.role, false);
    }
}
