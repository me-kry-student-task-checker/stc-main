package hu.me.iit.malus.thesis.user.event.listener;

import hu.me.iit.malus.thesis.user.client.EmailClient;
import hu.me.iit.malus.thesis.user.client.dto.Mail;
import hu.me.iit.malus.thesis.user.event.RegistrationCompletedEvent;
import hu.me.iit.malus.thesis.user.event.listener.config.ActivationConfig;
import hu.me.iit.malus.thesis.user.model.User;
import hu.me.iit.malus.thesis.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.UUID;

/**
 * Does all the action, that is needed after registration was completed.
 * Currently used for sending confirmation email for the user.
 *
 * @author Javorek Dénes
 */
@Component
@RequiredArgsConstructor
public class RegistrationCompletedListener implements
        ApplicationListener<RegistrationCompletedEvent> {

    private final UserService service;
    private final EmailClient emailClient;
    private final ActivationConfig activationConfig;

    @Override
    public void onApplicationEvent(RegistrationCompletedEvent event) {
        this.confirmRegistration(event);
    }

    private void confirmRegistration(RegistrationCompletedEvent event) {
        User user = event.getUser();
        String token = UUID.randomUUID().toString();
        service.createActivationToken(user, token);

        String recipientAddress = user.getEmail();
        String subject = "Registration Confirmation - " + activationConfig.getApplicationName();
        String confirmationUrl
                = activationConfig.getApplicationURL() + "/api/user/confirmation" + "?token=" + token;

        // This can be externalized via MessageSource, and get messages for different locales
        String message = "You can activate your account on the following link: ";

        emailClient.sendMail(new Mail(Collections.singletonList(recipientAddress), subject, message + confirmationUrl));
    }
}