package hu.me.iit.malus.thesis.user.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


/**
 * Representation of Admin type User
 *
 * @author Javorek Dénes
 */
@Getter @Setter
@ToString @EqualsAndHashCode
public class Admin extends User {
    public Admin(String id, String password, String firstName, String lastName) {
        super(id, password, firstName, lastName, UserRole.ADMIN, false);
    }
}
