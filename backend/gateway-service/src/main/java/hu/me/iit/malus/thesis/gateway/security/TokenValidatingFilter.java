package hu.me.iit.malus.thesis.gateway.security;

import hu.me.iit.malus.thesis.gateway.security.config.TokenAuthenticationFilterConfig;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * Custom filter, that finds and validates the JWT (by Authorization header) in the incoming request.
 * If the token is valid, the request will be forwarded to the destination service,
 * with the same included JWT  (by X-Auth-Token header).
 * If the token is missing from the request or invalid, the request will be blocked with Unauthorized error
 * @author Javorek Dénes
 */
@Slf4j
@Component
public class TokenValidatingFilter extends AbstractGatewayFilterFactory<TokenAuthenticationFilterConfig> {
    private static final String WWW_AUTH_HEADER = "WWW-Authenticate";
    private static final String BEARER = "Bearer";

    private TokenAuthenticationFilterConfig config;

    @Autowired
    public TokenValidatingFilter(TokenAuthenticationFilterConfig config) {
        super(TokenAuthenticationFilterConfig.class);
        this.config = config;
    }

    @Override
    public TokenAuthenticationFilterConfig newConfig() {
        return config;
    }

    /**
     * @see TokenValidatingFilter javaDoc
     * @param config
     * @return The filter
     */
    @Override
    public GatewayFilter apply(TokenAuthenticationFilterConfig config) {
        return (exchange, chain) -> {
            try {
                String token = this.extractJWTToken(exchange.getRequest());

                Jwts.parser()
                        .setSigningKey(config.getSecret().getBytes())
                        .parseClaimsJws(token);

                ServerHttpRequest request = exchange.getRequest().mutate().
                        header(config.getInnerTokenHeader(), token).
                        build();

                return chain.filter(exchange.mutate().request(request).build());
            } catch (JwtException e) {
                log.error("Request blocked to " + e.toString());
                return this.onError(exchange, e.getMessage());
            }
        };
    }

    /**
     * Handles any error during filter execution, sets Unauthorized status code,
     * and includes info message about token usage in WWW-Auth header
     * @param exchange
     * @param err The error message
     * @return
     */
    private Mono<Void> onError(ServerWebExchange exchange, String err)
    {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        response.getHeaders().add(WWW_AUTH_HEADER, this.formatErrorMsg(err));

        return response.setComplete();
    }

    private String extractJWTToken(ServerHttpRequest request)
    {
        if (!request.getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
            throw new JwtException("Token header is missing");
        }

        List<String> headers = request.getHeaders().get(HttpHeaders.AUTHORIZATION);

        if (headers.isEmpty()) {
            throw new JwtException("Token header is empty: " + request.getPath());
        }

        String tokenHeader = headers.get(0);

        if (!tokenHeader.startsWith(BEARER)) {
            throw new JwtException("Token header is invalid: " + request.getPath());
        }

        return tokenHeader.substring((BEARER + " ").length());
    }

    /**
     * Simple error message formatter. The error message gets extended with the page of JWT Standard
     * @param msg
     * @return Formatted string
     */
    private String formatErrorMsg(String msg)
    {
        return String.format("Bearer realm=\"acm.com\", " +
                "error=\"https://tools.ietf.org/html/rfc7519\", " +
                "error_description=\"%s\" ",  msg);
    }
}
