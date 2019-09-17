package hu.me.iit.malus.thesis.user.event.listener;

import hu.me.iit.malus.thesis.user.client.EmailClient;
import hu.me.iit.malus.thesis.user.client.dto.Mail;
import hu.me.iit.malus.thesis.user.event.RegistrationCompletedEvent;
import hu.me.iit.malus.thesis.user.model.User;
import hu.me.iit.malus.thesis.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.UUID;

@Component
public class RegistrationCompletedListener implements
        ApplicationListener<RegistrationCompletedEvent> {

    private UserService service;
    private EmailClient emailClient;

    @Autowired
    public RegistrationCompletedListener(UserService service, EmailClient emailClient) {
        this.service = service;
        this.emailClient = emailClient;
    }

    @Override
    public void onApplicationEvent(RegistrationCompletedEvent event) {
        this.confirmRegistration(event);
    }

    private void confirmRegistration(RegistrationCompletedEvent event) {
        User user = event.getUser();
        String token = UUID.randomUUID().toString();
        service.createActivationToken(user, token);

        String recipientAddress = user.getEmail();
        String subject = "Registration Confirmation - Student Task Checker";
        String confirmationUrl
                = event.getAppUrl() + "/?token=" + token;

        // This can be externalized via MessageSource, and get messages for different locales
        String message = "You can activate your account on the following ";

        Mail email = new Mail();
        email.setTo(Arrays.asList(recipientAddress));
        email.setSubject(subject);

        // Application URL (config) can be used here, if necessary
        email.setText(message + " rn" + confirmationUrl);

        emailClient.sendMail(email);
    }
}