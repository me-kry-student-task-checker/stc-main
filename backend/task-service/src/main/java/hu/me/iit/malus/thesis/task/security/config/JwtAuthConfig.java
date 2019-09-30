package hu.me.iit.malus.thesis.task.security.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Default configuration class, that provides all the required JWT properties.
 * Used for parsing and validating JWT from header
 * @author Javorek Dénes
 */
@Getter
@Component
public class JwtAuthConfig {
    @Value("${security.jwt.secret}")
    private String secret;

    @Value("${security.jwt.inner.header}")
    private String tokenHeader;
}
