package hu.me.iit.malus.thesis.email.service.exception;

/**
 * It is thrown if the attachments could not be read, or the mail could not be sent.
 *
 * @author Ilku Krisztian
 */
public class MailCouldNotBeSentException extends Exception {

    private static final String ERROR_MSG = "Mail could not be sent!";

    public MailCouldNotBeSentException(Throwable cause) {
        super(ERROR_MSG, cause);
    }

}
