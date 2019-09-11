package hu.me.iit.malus.thesis.gateway.security;

import hu.me.iit.malus.thesis.gateway.security.config.TokenAuthenticationFilterConfig;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
public class TokenAuthenticationFilter extends AbstractGatewayFilterFactory<TokenAuthenticationFilterConfig> {
    private static final String WWW_AUTH_HEADER = "WWW-Authenticate";
    private static final String X_AUTH_TOKEN = "X-Auth-Token";
    private TokenAuthenticationFilterConfig config;

    @Autowired
    public TokenAuthenticationFilter(TokenAuthenticationFilterConfig config) {
        super(TokenAuthenticationFilterConfig.class);
        this.config = config;
    }

    @Override
    public TokenAuthenticationFilterConfig newConfig() {
        return config;
    }

    @Override
    public GatewayFilter apply(TokenAuthenticationFilterConfig config) {
        return (exchange, chain) -> {

            try {
                String token = this.extractJWTToken(exchange.getRequest(), config);

                try {
                    Jwts.parser()
                            .setSigningKey(config.getSecret().getBytes())
                            .parseClaimsJws(token);

                    ServerHttpRequest request = exchange.getRequest().mutate().
                            header(X_AUTH_TOKEN, token).
                            build();

                    return chain.filter(exchange.mutate().request(request).build());
                } catch (Exception e) {
                    //TODO: Catch the possible exceptions one by one (dont catch Exception class)
                    throw new JwtException("Something went wrong during parsing JWT claims");
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

    private String extractJWTToken(ServerHttpRequest request, TokenAuthenticationFilterConfig config)
    {
        if (!request.getHeaders().containsKey(config.getHeader())) {
            throw new JwtException("Token header is missing");
        }

        List<String> headers = request.getHeaders().get(config.getHeader());

        if (headers.isEmpty()) {
            throw new JwtException("Token header is empty");
        }

        String tokenHeader = headers.get(0);

        if (!tokenHeader.startsWith(config.getPrefix())) {
            throw new JwtException("Token header is invalid");
        }

        return tokenHeader.replace(config.getPrefix(), "");
    }

    private String formatErrorMsg(String msg)
    {
        return String.format("Bearer realm=\"acm.com\", " +
                "error=\"https://tools.ietf.org/html/rfc7519\", " +
                "error_description=\"%s\" ",  msg);
    }
}
