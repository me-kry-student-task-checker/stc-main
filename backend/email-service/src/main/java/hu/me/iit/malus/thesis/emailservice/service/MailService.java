package hu.me.iit.malus.thesis.emailservice.service;

import hu.me.iit.malus.thesis.emailservice.model.Mail;
import org.springframework.mail.MailException;

/**
 * The interface Mail service.
 *
 * @author Ilku Krisztián
 */
public interface MailService {
    /**
     * Send email.
     *
     * @param mail the mail parameters
     * @throws MailException the mail exception
     */
    void sendEmail(Mail mail) throws MailException;
}
