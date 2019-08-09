package hu.me.iit.malus.thesis.email.service.impl;

import com.google.appengine.api.mail.MailService;
import com.google.appengine.api.mail.MailServiceFactory;
import hu.me.iit.malus.thesis.email.model.Mail;
import hu.me.iit.malus.thesis.email.model.exception.MailCouldNotBeSentException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * The type Mail service.
 *
 * @author Ilku Krisztián
 */
@Service
@Slf4j
public class MailServiceImpl implements hu.me.iit.malus.thesis.email.service.MailService {

    private MailService mailService = MailServiceFactory.getMailService();

    /**
     * Sends an email.
     *
     * @param mail The email to be sent.
     */
    @Override
    public void sendEmail(Mail mail) throws MailCouldNotBeSentException {
        MailService.Message email = new MailService.Message();
        email.setSender(mail.getFrom());
        email.setTo(mail.getTo());
        email.setSubject(mail.getSubject());
        email.setTextBody(mail.getText());

        if (mail.getReplyTo() != null && !mail.getReplyTo().isEmpty())
            email.setReplyTo(mail.getReplyTo());

        if (mail.getCcs() != null)
            email.setCc(mail.getCcs());

        if (mail.getBccs() != null)
            email.setBcc(mail.getBccs());

        if (mail.getAttachments() != null) {
            try {
                for (MultipartFile file : mail.getAttachments()) {
                    email.getAttachments().add(new MailService.Attachment(file.getName(), file.getBytes()));
                }
            } catch (IOException e) {
                log.error(e.getMessage());
                throw new MailCouldNotBeSentException();
            }
        }
        try {
            mailService.send(email);
        }
        catch (IOException e) {
            log.error(e.getMessage());
            throw new MailCouldNotBeSentException(e);
        }

    }

}
