package hu.me.iit.malus.thesis.user.security;


import com.fasterxml.jackson.databind.ObjectMapper;
import hu.me.iit.malus.thesis.user.controller.dto.LoginRequest;
import hu.me.iit.malus.thesis.user.security.config.JwtAuthConfig;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.Date;
import java.util.stream.Collectors;

/**
 * Custom filter, which purpose is to handle login event.
 * Gets the credentials from the login request,
 * Checks them with the help of our implementation of UserDetailsService,
 * If all good, provides a JWT and returns it in the Authorization header.
 * Otherwise returns 401 - Unauthorized status
 */
public class UsernameAndPasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private AuthenticationManager authManager;
    private JwtAuthConfig jwtConfig;

    @Autowired
    public UsernameAndPasswordAuthenticationFilter(AuthenticationManager authManager, JwtAuthConfig jwtAuthConfig) {
        this.authManager = authManager;
        this.jwtConfig = jwtAuthConfig;

        // By default, UsernamePasswordAuthenticationFilter listens to "/login" path.
        // In our case, it is not correct. So, we need to override the defaults.
        this.setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher(jwtAuthConfig.getUri(), "POST"));
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {

        try {
            LoginRequest requestCredentials = new ObjectMapper().readValue(request.getInputStream(), LoginRequest.class);

            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                    requestCredentials.getEmail(), requestCredentials.getPassword(), Collections.emptyList());

            return authManager.authenticate(authToken);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Called when the authentication was successful and the user can be logged in.
     * @param request
     * @param response
     * @param chain
     * @param authenticatedUser Which is successfully logged in now
     */
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
                                            Authentication authenticatedUser) throws IOException, ServletException {
        Long now = System.currentTimeMillis();
        String token = Jwts.builder()
                .setSubject(authenticatedUser.getName())
                .claim("roles", authenticatedUser.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                .setIssuedAt(new Date(now))
                .setExpiration(new Date(now + jwtConfig.getExpiration() * 1000))  // in milliseconds
                .signWith(SignatureAlgorithm.HS512, jwtConfig.getSecret().getBytes())
                .compact();

        response.addHeader(HttpHeaders.AUTHORIZATION, token);
    }
}
