package hu.me.iit.malus.thesis.email.service;

import hu.me.iit.malus.thesis.email.model.Mail;
import hu.me.iit.malus.thesis.email.model.exception.MailCouldNotBeSentException;

/**
 * The interface Mail service.
 *
 * @author Ilku Krisztián
 */
public interface MailService {
    /**
     * Sends a email.
     *
     * @param mail the mail parameters
     */
    void sendEmail(Mail mail) throws MailCouldNotBeSentException;
}
