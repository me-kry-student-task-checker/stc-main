package hu.me.iit.malus.thesis.user.security.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Default configuration class, that provides all the required JWT properties,
 * used in the authentication process
 * @author Javorek Dénes
 */
@Getter
@Component
public class JwtAuthConfig {
    @Value("${security.jwt.uri}")
    private String Uri;

    @Value("${security.jwt.expiration}")
    private int expiration;

    @Value("${security.jwt.secret}")
    private String secret;

    @Value("${security.jwt.inner.header}")
    private String tokenHeader;
}
