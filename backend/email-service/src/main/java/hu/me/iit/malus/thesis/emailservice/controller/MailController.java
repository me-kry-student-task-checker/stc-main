package hu.me.iit.malus.thesis.emailservice.controller;

import hu.me.iit.malus.thesis.emailservice.model.Mail;
import hu.me.iit.malus.thesis.emailservice.service.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * The type Mail controller.
 */
@RestController
public class MailController {

    private MailService mailService;

    /**
     * Sets notification service.
     *
     * @param mailService the notification service
     */
    @Autowired
    public MailController(MailService mailService) {
        this.mailService = mailService;
    }

    /**
     * Send string.
     *
     * @param mail the mail
     * @return the string
     */
    @PostMapping("/sendMail")
    public String send(@RequestBody Mail mail) {
        try {
            mailService.sendEmail(mail);
        } catch (MailException e) {
            e.printStackTrace();
        }
        return "Mail Sent";
    }
}
