package hu.me.iit.malus.thesis.user.model;

import lombok.*;

import javax.persistence.Entity;


/**
 * Representation of Admin type User
 * @author Javorek Dénes
 */
@Entity
@Getter @Setter
@ToString
@NoArgsConstructor
public class Admin extends User {
    public Admin(String email, String password, String firstName, String lastName) {
        super(email, password, firstName, lastName, UserRole.ADMIN, false);
    }
}
