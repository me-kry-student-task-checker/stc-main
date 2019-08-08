package hu.me.iit.malus.thesis.email.controller;

import hu.me.iit.malus.thesis.email.model.Mail;
import hu.me.iit.malus.thesis.email.service.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * Mail controller.
 *
 * @author Ilku Krisztián
 */
@RestController
@Validated
public class MailController {

    private MailService mailService;

    /**
     * Sets mail service.
     *
     * @param mailService the mail service
     */
    @Autowired
    public MailController(MailService mailService) {
        this.mailService = mailService;
    }

    /**
     * Send string.
     *
     * @param mail the mail to be sent
     * @return the string
     */
    @PostMapping("/sendMail")
    public ResponseEntity<String> send(@Valid @RequestBody Mail mail) {
        mailService.sendEmail(mail);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body("Email successfully sent!");
    }
}