package hu.me.iit.malus.thesis.user.model;

import java.util.List;

/**
 * Representation of a Student type User
 *
 * @author Javorek Dénes
 */
public class Admin extends User {
    public Admin(String id, String password, String firstName, String lastName) {
        super(id, password, firstName, lastName, UserRole.ADMIN);
    }
}
