package hu.me.iit.malus.thesis.email.service.impl;

import hu.me.iit.malus.thesis.email.model.Mail;
import hu.me.iit.malus.thesis.email.model.exception.MailCouldNotBeSentException;
import hu.me.iit.malus.thesis.email.service.MailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

/**
 * The type Mail service.
 *
 * @author Ilku Krisztián
 */
@Service
@Slf4j
public class MailServiceImpl implements MailService {


    private JavaMailSender javaMailSender;


    @Autowired
    public MailServiceImpl(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    /**
     * Sends an email.
     *
     * @param mail The email to be sent.
     */
    @Override
    public void sendEmail(Mail mail) throws MailCouldNotBeSentException {
        SimpleMailMessage email = new SimpleMailMessage();
        
        email.setTo(mail.getTo().toArray(new String[0]));
        if (mail.getReplyTo() != null)
            email.setReplyTo(mail.getReplyTo());

        if (mail.getCcs() != null)
            email.setCc(mail.getCcs());

        if (mail.getBccs() != null)
            email.setBcc(mail.getBccs());

        email.setSubject(mail.getSubject());
        email.setText(mail.getText());

        try {
            javaMailSender.send(email);
        } catch (MailException e) {
            log.error(e.getMessage());
            throw new MailCouldNotBeSentException(e);
        }
    }
}
