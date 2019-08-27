package hu.me.iit.malus.thesis.gateway.security;

import io.jsonwebtoken.*;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

@Slf4j
@Component
public class TokenAuthenticationFilter extends AbstractGatewayFilterFactory<TokenAuthenticationFilter.Config> {
    private static final String WWW_AUTH_HEADER = "WWW-Authenticate";
    private static final String X_JWT_SUB_HEADER = "X-jwt-sub";

    public TokenAuthenticationFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {

            try {
                String token = this.extractJWTToken(exchange.getRequest(), config);

                try {
                    Claims claims = Jwts.parser()
                            .setSigningKey(config.getSecret().getBytes())
                            .parseClaimsJws(token)
                            .getBody();

                    ServerHttpRequest request = exchange.getRequest().mutate().
                            header(X_JWT_SUB_HEADER, claims.getSubject()).
                            //TODO: Check what SecurityContext.setAuthentication did, how to send SecurityContext with request
                            build();

                    return chain.filter(exchange.mutate().request(request).build());
                } catch (Exception e) {
                    //TODO: Catch the possible exceptions one by one (dont catch Exception class)
                    throw new JwtException("Something went wrong during parsing JWT claims.");
                }

            } catch (JwtException e) {
                log.error(e.toString());
                return this.onError(exchange, e.getMessage());
            }
        };
    }

    private Mono<Void> onError(ServerWebExchange exchange, String err)
    {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        response.getHeaders().add(WWW_AUTH_HEADER, this.formatErrorMsg(err));

        return response.setComplete();
    }

    private String extractJWTToken(ServerHttpRequest request, Config config)
    {
        List<String> headers = request.getHeaders().get(config.getHeader());
        if (headers.isEmpty()) {
            throw new JwtException("Token header is missing");
        }

        String header = headers.get(0).trim();

        if (!header.startsWith(config.getPrefix())) {
            throw new JwtException("Token header is invalid");
        }

        return header.replace(config.getPrefix(), "");
    }

    private String formatErrorMsg(String msg)
    {
        return String.format("Bearer realm=\"acm.com\", " +
                "error=\"https://tools.ietf.org/html/rfc7519\", " +
                "error_description=\"%s\" ",  msg);
    }

    @Getter
    public static class Config {
        private static final String name = "TokenAuthenticationFilter";
        private static final String value = "Validates request, on sending JWT token in header";

        @Value("${security.jwt.uri}")
        private String Uri;

        @Value("${security.jwt.header}")
        private String header;

        @Value("${security.jwt.prefix}")
        private String prefix;

        @Value("${security.jwt.secret}")
        private String secret;
    }

}
