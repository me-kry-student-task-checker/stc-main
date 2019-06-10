package hu.me.iit.malus.thesis.emailservice.service.impl;

import hu.me.iit.malus.thesis.emailservice.model.Mail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

/**
 * The type Mail service.
 *
 * @author Szőke Attila
 */
@Service
public class MailServiceImpl implements hu.me.iit.malus.thesis.emailservice.service.MailService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MailServiceImpl.class);

    private JavaMailSender javaMailSender;

    /**
     * Instantiates a new Mail service.
     *
     * @param javaMailSender the java mail sender
     */
    @Autowired
    public MailServiceImpl(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    /**
     * Sends an email.
     *
     * @param mailParameters the necessary parameters for an email
     */
    @Override
    public void sendEmail(Mail mailParameters) {
        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setTo(mailParameters.getTo());
        mail.setSubject(mailParameters.getSubject());
        mail.setText(mailParameters.getText());
        if (mailParameters.getBccs().length != 0) {
            mail.setBcc(mailParameters.getBccs());
        }
        try {
            javaMailSender.send(mail);
        } catch (MailException e) {
            LOGGER.error(e.getMessage());
        }
        LOGGER.info("E-mail sent: {}", mailParameters);
    }

}
