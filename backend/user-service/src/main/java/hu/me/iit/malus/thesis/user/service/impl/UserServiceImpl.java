package hu.me.iit.malus.thesis.user.service.impl;

import hu.me.iit.malus.thesis.user.controller.dto.RegistrationRequest;
import hu.me.iit.malus.thesis.user.model.ActivationToken;
import hu.me.iit.malus.thesis.user.model.exception.EmailExistsException;
import hu.me.iit.malus.thesis.user.model.User;
import hu.me.iit.malus.thesis.user.model.UserRole;
import hu.me.iit.malus.thesis.user.repository.ActivationTokenRepository;
import hu.me.iit.malus.thesis.user.repository.UserRepository;
import hu.me.iit.malus.thesis.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Default implementation of UserService
 * @author Javorek Dénes
 */
@Service
public class UserServiceImpl implements UserService {
    UserRepository userRepository;
    ActivationTokenRepository tokenRepository;
    BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, ActivationTokenRepository tokenRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Registers a new user into the database, throws exception if its email already registered
     * @param request Contains all the required information for registration.
     * @return The registered User
     * @throws EmailExistsException
     */
    @Override
    public User registerNewUserAccount(RegistrationRequest request) throws EmailExistsException {
        if (emailExists(request.getEmail())) {
            throw new EmailExistsException(
                    "There is already an account with that email address: " + request.getEmail());
        }

        User user = new User(request.getEmail(), passwordEncoder.encode(request.getPassword()),
                request.getFirstName(), request.getLastName(), UserRole.valueOf(request.getRole()), false);
        return userRepository.save(user);
    }

    /**
     * Creates and saves a new activation token for a user
     * @param user Owner of the token
     * @param token
     */
    @Override
    public void createActivationToken(User user, String token) {
        final ActivationToken userToken = ActivationToken.of(token, user);
        tokenRepository.save(userToken);
    }

    /**
     * Checks whether a given email address already exists in our system, or not.
     * @param email To check
     * @return True if email already exists, false otherwise
     */
    private boolean emailExists(String email) {
        User user = userRepository.findByEmail(email);
        if (user != null) {
            return true;
        }
        return false;
    }
}
