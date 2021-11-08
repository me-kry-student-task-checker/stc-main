package hu.me.iit.malus.thesis.email.service.impl;

import hu.me.iit.malus.thesis.email.controller.dto.MailDto;
import hu.me.iit.malus.thesis.email.service.MailService;
import hu.me.iit.malus.thesis.email.service.exception.MailCouldNotBeSentException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

/**
 * The default implementation of MailService.
 *
 * @author Ilku Krisztián
 * @author Attila Szőke
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class MailServiceImpl implements MailService {

    public static final String FROM = "STC-Email-Service";

    private final JavaMailSender javaMailSender;

    /**
     * {@inheritDoc}
     */
    @Override
    public void sendEmail(MailDto dto) throws MailCouldNotBeSentException {
        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setTo(dto.getTo().toArray(new String[0]));
        mail.setFrom(FROM);
        mail.setReplyTo(dto.getReplyTo());
        mail.setCc(dto.getCcs());
        mail.setBcc(dto.getBccs());
        mail.setSubject(dto.getSubject());
        mail.setText(dto.getText());
        try {
            javaMailSender.send(mail);
        } catch (MailException e) {
            log.error(e.getMessage());
            throw new MailCouldNotBeSentException(e);
        }
    }
}
