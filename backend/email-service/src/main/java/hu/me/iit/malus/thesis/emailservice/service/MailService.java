package hu.me.iit.malus.thesis.emailservice.service;

import hu.me.iit.malus.thesis.emailservice.model.Mail;
import org.springframework.mail.MailException;

public interface MailService {
    void sendEmail(Mail mailParameters) throws MailException;
}
