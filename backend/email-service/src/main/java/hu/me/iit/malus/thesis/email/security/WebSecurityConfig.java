package hu.me.iit.malus.thesis.email.security;

import hu.me.iit.malus.thesis.email.security.config.JwtAuthConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletResponse;

@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    private JwtAuthConfig jwtAuthConfig;

    @Autowired
    public WebSecurityConfig(JwtAuthConfig jwtAuthConfig) {
        this.jwtAuthConfig = jwtAuthConfig;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()

                // Session won't be used to store user's state.
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()

                // Handles Unauthorized requests
                .exceptionHandling().authenticationEntryPoint((req, rsp, e) -> rsp.sendError(HttpServletResponse.SC_UNAUTHORIZED))
                .and()

                // New filter to validate user credentials and generate token
                .addFilterAfter(new JwtAuthorizationFilter(jwtAuthConfig), UsernamePasswordAuthenticationFilter.class)

                // Authorization requests configuration
                .authorizeRequests()

                // Allow all requests going for send,
                // because it is used even without authenticated user. (During registration)
                .antMatchers(HttpMethod.POST, "/api/mail/send").permitAll()

                // Any other request must be authenticated
                .anyRequest().authenticated();
    }
}
