package hu.me.iit.malus.thesis.emailservice.service.impl;

import hu.me.iit.malus.thesis.emailservice.model.Mail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

/**
 * The type Mail service.
 */
@Service
public class MailServiceImpl implements hu.me.iit.malus.thesis.emailservice.service.MailService {

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
     * @throws MailException if something goes wrong it's thrown
     */
    @Override
    public void sendEmail(Mail mailParameters) throws MailException {
        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setTo(mailParameters.getTo());
        mail.setSubject(mailParameters.getSubject());
        mail.setText(mailParameters.getText());
        if (mailParameters.getBccs().length != 0) {
            mail.setBcc(mailParameters.getBccs());
        }
        javaMailSender.send(mail);
    }

}
