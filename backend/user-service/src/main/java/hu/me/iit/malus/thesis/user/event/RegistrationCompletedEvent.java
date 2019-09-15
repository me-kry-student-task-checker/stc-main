package hu.me.iit.malus.thesis.user.event;

import hu.me.iit.malus.thesis.user.model.User;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.util.Locale;

/**
 * Application event, which is published after registration to indicate for post-registration tasks
 * @author Javorek Dénes
 */
@Getter
public class RegistrationCompletedEvent extends ApplicationEvent {
    private final String appUrl;

    // It can be used later to send emails in different languages
    private final Locale locale;
    private final User user;

    public RegistrationCompletedEvent(final User user, final Locale locale, final String appUrl) {
        super(user);
        this.user = user;
        this.locale = locale;
        this.appUrl = appUrl;
    }
}
