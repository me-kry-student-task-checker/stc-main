package hu.me.iit.malus.thesis.user.event.listener;

import java.util.List;
import hu.me.iit.malus.thesis.user.event.RegistrationCompletedEvent;
import hu.me.iit.malus.thesis.user.model.Student;
import org.junit.Before;
import org.junit.Test;
import hu.me.iit.malus.thesis.user.model.User;

import static org.mockito.Mockito.mock;

public class RegistrationCompletedListenerTest {

    private RegistrationCompletedListener registrationCompletedListener;

    @Before
    public void setUp() throws Exception {
        this.registrationCompletedListener = mock(RegistrationCompletedListener.class);
    }

    @Test
    public void onApplicationEvent() {
        User user = new Student("user@example.com", "pw1", "fName1", "lName1", List.of());

        RegistrationCompletedEvent registrationCompletedEvent = new RegistrationCompletedEvent(user);
        registrationCompletedListener.onApplicationEvent(registrationCompletedEvent);
    }

}
