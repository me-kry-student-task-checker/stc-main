package hu.me.iit.malus.thesis.emailservice.service.impl;

import com.google.appengine.api.mail.MailService;
import com.google.appengine.api.mail.MailServiceFactory;
import hu.me.iit.malus.thesis.emailservice.model.Mail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * The type Mail service.
 *
 * @author Ilku Krisztián
 */
@Service
public class MailServiceImpl implements hu.me.iit.malus.thesis.emailservice.service.MailService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MailServiceImpl.class);
    private MailService mailService;

    /**
     * Instantiates a new Mail service.
     */
    @Autowired
    public MailServiceImpl() {
        this.mailService = MailServiceFactory.getMailService();
    }

    /**
     * Sends an email.
     *
     * @param mail the necessary parameters for an email
     */
    @Override
    public void sendEmail(Mail mail) {
        MailService.Message email = new MailService.Message();
        if (mail.getFrom() != null && !mail.getFrom().equals(""))
            email.setSender(mail.getFrom());

        if (mail.getTo() != null)
            email.setTo(mail.getTo());

        if (mail.getReplyTo() != null && !mail.getReplyTo().equals(""))
            email.setReplyTo(mail.getReplyTo());

        if (mail.getCcs() != null)
            email.setCc(mail.getCcs());

        if (mail.getBccs() != null)
            email.setBcc(mail.getBccs());

        if (mail.getSubject() != null && !mail.getSubject().equals(""))
            email.setSubject(mail.getSubject());

        if (mail.getText() != null && !mail.getText().equals(""))
            email.setTextBody(mail.getText());

        if (mail.getAttachments() != null) {

            try {
                for (MultipartFile file : mail.getAttachments()) {
                    email.getAttachments().add(new MailService.Attachment(file.getName(), file.getBytes()));
                }
            } catch (IOException e) {
                LOGGER.error(e.getMessage());
                e.printStackTrace();
            }
        }
        try {
            mailService.send(email);
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
        }
    }

}
