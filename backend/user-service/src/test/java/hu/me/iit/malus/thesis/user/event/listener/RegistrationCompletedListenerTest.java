package hu.me.iit.malus.thesis.user.event.listener;

import java.util.List;

import hu.me.iit.malus.thesis.user.client.EmailClient;
import hu.me.iit.malus.thesis.user.event.RegistrationCompletedEvent;
import hu.me.iit.malus.thesis.user.event.listener.config.ActivationConfig;
import hu.me.iit.malus.thesis.user.model.Student;
import hu.me.iit.malus.thesis.user.service.UserService;
import org.junit.Before;
import org.junit.Test;
import hu.me.iit.malus.thesis.user.model.User;

import static org.mockito.Mockito.*;

public class RegistrationCompletedListenerTest {

    private UserService service;
    private EmailClient emailClient;
    private ActivationConfig activationConfig;
    private RegistrationCompletedListener registrationCompletedListener;

    @Before
    public void setUp() throws Exception {
        this.service = mock(UserService.class);
        this.emailClient = mock(EmailClient.class);
        this.activationConfig = mock(ActivationConfig.class);
        this.registrationCompletedListener =
                new RegistrationCompletedListener(service, emailClient, activationConfig);
    }

    @Test
    public void onApplicationEvent() {
        User user = new Student("user@example.com", "pw1", "fName1", "lName1", List.of());

        RegistrationCompletedEvent registrationCompletedEvent = new RegistrationCompletedEvent(user);

        registrationCompletedListener.onApplicationEvent(registrationCompletedEvent);
    }

}
