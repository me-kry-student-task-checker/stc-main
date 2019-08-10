package hu.me.iit.malus.thesis.email.service;

import hu.me.iit.malus.thesis.email.model.Mail;
import hu.me.iit.malus.thesis.email.model.exception.MailCouldNotBeSentException;
import org.springframework.mail.MailException;

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
     * @throws MailException the mail exception
     */
    void sendEmail(Mail mail) throws MailCouldNotBeSentException;
}
