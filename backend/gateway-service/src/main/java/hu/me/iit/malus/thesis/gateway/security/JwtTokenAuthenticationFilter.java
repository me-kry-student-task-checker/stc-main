package hu.me.iit.malus.thesis.gateway.security;

import hu.me.iit.malus.thesis.gateway.security.config.JwtGatewayConfig;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
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

public class JwtTokenAuthenticationFilter extends OncePerRequestFilter {
    private final JwtGatewayConfig jwtGatewayConfig;

    public JwtTokenAuthenticationFilter(JwtGatewayConfig jwtGatewayConfig) {
        this.jwtGatewayConfig = jwtGatewayConfig;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        // Parsing the token
        String tokenHeader = request.getHeader(jwtGatewayConfig.getHeader());

        if(tokenHeader == null || !tokenHeader.startsWith(jwtGatewayConfig.getPrefix())) {
            chain.doFilter(request, response);
            return;
        }

        String token = tokenHeader.replace(jwtGatewayConfig.getPrefix(), "");

        // Getting and validating claims
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(jwtGatewayConfig.getSecret().getBytes())
                    .parseClaimsJws(token)
                    .getBody();

            String username = claims.getSubject();
            if(username != null) {
                List<String> authorities = (List<String>) claims.get("authorities");

                UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                        username, null, authorities.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList()));

                SecurityContextHolder.getContext().setAuthentication(auth);
            }

        } catch (Exception e) {
            SecurityContextHolder.clearContext();
        }

        chain.doFilter(request, response);
    }
}
