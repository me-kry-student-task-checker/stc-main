package hu.me.iit.malus.thesis.user.event;

import hu.me.iit.malus.thesis.user.model.User;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * Application event, which is published after registration to indicate for post-registration tasks
 * @author Javorek Dénes
 */
@Getter
public class RegistrationCompletedEvent extends ApplicationEvent {

    private final User user;

    public RegistrationCompletedEvent(final User user) {
        super(user);
        this.user = user;
    }
}
