package hu.me.iit.malus.thesis.gateway.security;

import hu.me.iit.malus.thesis.gateway.security.config.JwtGatewayConfig;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.endpoint.SecurityContext;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractNameValueGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

@Slf4j
public class TokenAuthenticationFilter extends AbstractNameValueGatewayFilterFactory {
    private static final String WWW_AUTH_HEADER = "WWW-Authenticate";
    private static final String X_JWT_SUB_HEADER = "X-jwt-sub";

    private final JwtGatewayConfig jwtGatewayConfig;

    @Autowired
    public TokenAuthenticationFilter(JwtGatewayConfig jwtGatewayConfig) {
        this.jwtGatewayConfig = jwtGatewayConfig;
    }

    @Override
    public GatewayFilter apply(NameValueConfig config) {
        return (exchange, chain) -> {

            try {
                String token = this.extractJWTToken(exchange.getRequest());
                try {
                    Claims claims = Jwts.parser()
                            .setSigningKey(jwtGatewayConfig.getSecret().getBytes())
                            .parseClaimsJws(token)
                            .getBody();

                    ServerHttpRequest request = exchange.getRequest().mutate().
                            header(X_JWT_SUB_HEADER, claims.getSubject()).
                            //TODO: Set Authorities header, check what SecurityContext.setAuthentication did
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

    private String extractJWTToken(ServerHttpRequest request)
    {
        if (!request.getHeaders().containsKey("Authorization")) {
            throw new JwtException("Authorization header is missing");
        }

        List<String> headers = request.getHeaders().get("Authorization");
        if (headers.isEmpty()) {
            throw new JwtException("Authorization header is empty");
        }

        String credential = headers.get(0).trim();
        String[] components = credential.split("\\s");

        if (components.length != 2) {
            throw new MalformedJwtException("Malformat Authorization content");
        }

        if (!components[0].equals("Bearer")) {
            throw new MalformedJwtException("Bearer is needed");
        }

        return components[1].trim();
    }

    private String formatErrorMsg(String msg)
    {
        return String.format("Bearer realm=\"acm.com\", " +
                "error=\"https://tools.ietf.org/html/rfc7519\", " +
                "error_description=\"%s\" ",  msg);
    }
}
