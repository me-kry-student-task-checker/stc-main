package hu.me.iit.malus.thesis.user.security.config;

import lombok.Getter;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Default configuration class, that loads all the required JWT properties used in this microservice
 * @author Javorek Dénes
 */
@Getter
@ToString
@Component
public class JwtAuthConfig {
    @Value("${security.jwt.uri}")
    private String Uri;

    @Value("${security.jwt.header}")
    private String header;

    @Value("${security.jwt.prefix}")
    private String prefix;

    @Value("${security.jwt.expiration}")
    private int expiration;

    @Value("${security.jwt.secret}")
    private String secret;
}
