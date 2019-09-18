package hu.me.iit.malus.thesis.user.model.exception;

/**
 * Exception that is thrown when a user cannot be found by querying an email address.
 * @author Javorek Dénes
 */
public class UserNotFoundException extends Exception {
    private static final String MSG = "User with this email address: {}, cannot be found!";

    public UserNotFoundException(String requestedEmail) {
        super(String.format(MSG, requestedEmail));
    }
}
