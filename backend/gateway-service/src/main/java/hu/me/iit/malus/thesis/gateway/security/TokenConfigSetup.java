package hu.me.iit.malus.thesis.gateway.security;

import hu.me.iit.malus.thesis.gateway.security.config.JwtGatewayConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletResponse;

@EnableWebSecurity
public class TokenConfigSetup extends WebSecurityConfigurerAdapter {

    @Autowired
    private JwtGatewayConfig jwtGatewayConfig;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()

                // Session won't be used to store user's state.
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()

                // Handles Unauthorized requests
                .exceptionHandling().authenticationEntryPoint((req, rsp, e) ->
                    rsp.sendError(HttpServletResponse.SC_UNAUTHORIZED))
                .and()

                // New filter to validate the tokens with every request
                .addFilterAfter(new JwtTokenAuthenticationFilter(jwtGatewayConfig), UsernamePasswordAuthenticationFilter.class)

                // Authorization requests configuration
                .authorizeRequests()

                // Allow all requests going for authorization
                .antMatchers(HttpMethod.POST, jwtGatewayConfig.getUri()).permitAll()
                // Other routes can be whitelisted if needed, like:
                //.antMatchers("/courses" + "/all/**").hasRole("ADMIN")

                // Any other request must be authenticated
                .anyRequest().authenticated();
    }

    @Bean
    public JwtGatewayConfig jwtConfig() {
        return new JwtGatewayConfig();
    }
}
