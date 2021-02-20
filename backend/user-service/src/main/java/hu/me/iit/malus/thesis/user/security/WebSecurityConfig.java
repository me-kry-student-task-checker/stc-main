package hu.me.iit.malus.thesis.user.security;

import hu.me.iit.malus.thesis.user.security.config.JwtAuthConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.servlet.http.HttpServletResponse;

@Slf4j
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private UserDetailsService userDetailsService;
    private JwtAuthConfig jwtAuthConfig;
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public WebSecurityConfig(@Qualifier("customUserDetailsService") UserDetailsService userDetailsService, JwtAuthConfig jwtAuthConfig, BCryptPasswordEncoder passwordEncoder) {
        this.userDetailsService = userDetailsService;
        this.jwtAuthConfig = jwtAuthConfig;
        this.passwordEncoder = passwordEncoder;
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
                .addFilter(new UsernameAndPasswordAuthenticationFilter(authenticationManager(), jwtAuthConfig))
                .addFilterAfter(new JwtAuthorizationFilter(jwtAuthConfig), UsernameAndPasswordAuthenticationFilter.class)
                // Authorization requests configuration
                .authorizeRequests()

                // Allow all requests going for authorization, registration, and confirmation
                .antMatchers(HttpMethod.POST, jwtAuthConfig.getUri()).permitAll()
                .antMatchers(HttpMethod.POST, "/api/user/registration").permitAll()
                .antMatchers(HttpMethod.GET, "/api/user/confirmation*").permitAll()

                // Any other request must be authenticated
                .anyRequest().authenticated();
    }

    // Spring has UserDetailsService interface, which can be overrode to provide our implementation for fetching user data from any source.
    // The UserDetailsService object is used by the auth manager during authentication so it needs to be set up.
    // In addition, we need to define the password passwordEncoder also. So, auth manager can compare and verify passwords.
    @Override
    @Autowired
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
    }
}
