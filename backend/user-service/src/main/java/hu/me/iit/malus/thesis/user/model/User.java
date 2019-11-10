package hu.me.iit.malus.thesis.user.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Objects;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

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
@ToString
public abstract class User
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return Objects.equal(email, user.email);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(email);
    }
}
