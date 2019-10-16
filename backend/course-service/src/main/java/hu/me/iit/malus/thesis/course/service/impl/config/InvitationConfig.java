package hu.me.iit.malus.thesis.course.service.impl.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
public class InvitationConfig {
    @Value("${application.name}")
    private String applicationName;

    @Value("${application.address}")
    private String applicationURL;
}
