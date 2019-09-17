package hu.me.iit.malus.thesis.user.service;

import hu.me.iit.malus.thesis.user.controller.dto.RegistrationRequest;
import hu.me.iit.malus.thesis.user.model.exception.EmailExistsException;
import hu.me.iit.malus.thesis.user.model.User;

/**
 * Defines all the necessary operations for User service implementations
 * @author Javorek Dénes
 */
public interface UserService {
    User registerNewUserAccount(RegistrationRequest registrationRequest)
            throws EmailExistsException;

    void createActivationToken(User user, String token);

    boolean activateUser(String token);
}
