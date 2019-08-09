package hu.me.iit.malus.thesis.email.model.exception;

/**
 * @author Ilku Krisztian
 * It is thrown if the attachments could not be read, or the mail could not be sent.
 */
public class MailCouldNotBeSentException extends Exception {

    private static final String ERROR_MSG = "Mail could not be sent!";

    public MailCouldNotBeSentException() {
        super(ERROR_MSG);
    }

    /**
     *@param cause the cause (which is saved for later retrieval by the
     *         {@link #getCause()} method).  (A <tt>null</tt> value is
     *         permitted, and indicates that the cause is nonexistent or
     *         unknown.)
     */
    public MailCouldNotBeSentException(Throwable cause) {
        super(ERROR_MSG, cause);
    }

}
