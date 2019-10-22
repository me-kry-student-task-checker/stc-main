package hu.me.iit.malus.thesis.filemanagement.security;

import hu.me.iit.malus.thesis.filemanagement.security.config.JwtAuthConfig;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Custom filter, which authenticates the user's request, if it contains a valid JWT.
 * A copy of this filter must be included in every microservice (not an OOP scenario),
 * as all have separate SecurityContexts.
 * @author Javorek Dénes
 */
@Slf4j
public class JwtAuthorizationFilter extends OncePerRequestFilter {
    private JwtAuthConfig config;

    @Autowired
    public JwtAuthorizationFilter(JwtAuthConfig config) {
        this.config = config;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        try {
            String token = getTokenFromRequest(request);

            Claims claims = Jwts.parser()
                    .setSigningKey(config.getSecret().getBytes())
                    .parseClaimsJws(token)
                    .getBody();

            String email = claims.getSubject();
            List<String> authorities = (List<String>) claims.get("roles");

            UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                    email,
                    null,
                    authorities
                            .stream()
                            .map(SimpleGrantedAuthority::new)
                            .collect(Collectors.toList()));

            SecurityContextHolder.getContext().setAuthentication(auth);
        } catch (JwtException e) {
            log.warn("Token header cannot be parsed, request is unauthenticated: " + e.getMessage());
            SecurityContextHolder.clearContext();
        }
        chain.doFilter(request, response);
    }

    private String getTokenFromRequest(HttpServletRequest request)
    {
        String token = request.getHeader(config.getTokenHeader());

        if (token == null || token.length() == 0) {
            throw new JwtException("Token header is empty");
        }
        return token;
    }
}
