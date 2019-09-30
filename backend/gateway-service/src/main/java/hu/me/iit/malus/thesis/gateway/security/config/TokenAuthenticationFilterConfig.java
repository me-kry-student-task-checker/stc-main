package hu.me.iit.malus.thesis.gateway.security.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Component
public class TokenAuthenticationFilterConfig {
    private static final String name = "TokenAuthenticationFilter";
    private static final String value = "Validates request, on sending JWT token in header";

    @Value("${security.jwt.secret}")
    private String secret;

    @Value("${security.jwt.inner.header}")
    private String innerTokenHeader;
}
