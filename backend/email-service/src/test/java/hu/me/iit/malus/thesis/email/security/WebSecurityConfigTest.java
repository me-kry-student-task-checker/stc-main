package hu.me.iit.malus.thesis.email.security;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.annotation.web.configurers.ExceptionHandlingConfigurer;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.security.config.annotation.web.configurers.SessionManagementConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class WebSecurityConfigTest {

    @Mock
    HttpSecurity httpSecurity;

    @InjectMocks
    WebSecurityConfig webSecurityConfig;

    @Test
    public void configure() throws Exception {
        CsrfConfigurer<HttpSecurity> csrf = mock(CsrfConfigurer.class);
        when(csrf.disable()).thenReturn(httpSecurity);
        when(httpSecurity.csrf()).thenReturn(csrf);

        SessionManagementConfigurer<HttpSecurity> sessionManagement = mock(SessionManagementConfigurer.class);
        when(sessionManagement.sessionCreationPolicy(any())).thenReturn(sessionManagement);
        when(sessionManagement.and()).thenReturn(httpSecurity);
        when(httpSecurity.sessionManagement()).thenReturn(sessionManagement);

        ExceptionHandlingConfigurer<HttpSecurity> exceptionHandling = mock(ExceptionHandlingConfigurer.class);
        when(exceptionHandling.authenticationEntryPoint(any())).thenReturn(exceptionHandling);
        when(exceptionHandling.and()).thenReturn(httpSecurity);
        when(httpSecurity.exceptionHandling()).thenReturn(exceptionHandling);

        when(httpSecurity.addFilterAfter(any(), any())).thenReturn(httpSecurity);


        ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry authorizeRequests = mock(ExpressionUrlAuthorizationConfigurer.ExpressionInterceptUrlRegistry.class);
        ExpressionUrlAuthorizationConfigurer<HttpSecurity>.AuthorizedUrl url = mock(ExpressionUrlAuthorizationConfigurer.AuthorizedUrl.class);
        when(authorizeRequests.antMatchers(any(HttpMethod.class), anyString())).thenReturn(url);
        when(url.permitAll()).thenReturn(authorizeRequests);
        when(authorizeRequests.anyRequest()).thenReturn(url);
        when(url.authenticated()).thenReturn(authorizeRequests);
        when(httpSecurity.authorizeRequests()).thenReturn(authorizeRequests);


        webSecurityConfig.configure(httpSecurity);


        verify(httpSecurity, times(1)).csrf();
        verify(csrf, times(1)).disable();

        verify(httpSecurity, times(1)).sessionManagement();
        verify(sessionManagement, times(1)).sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        verify(sessionManagement, times(1)).and();

        verify(httpSecurity, times(1)).exceptionHandling();
        verify(exceptionHandling, times(1)).authenticationEntryPoint(any());
        verify(exceptionHandling, times(1)).and();

        verify(httpSecurity, times(1)).addFilterAfter(
            any(JwtAuthorizationFilter.class),
            eq(UsernamePasswordAuthenticationFilter.class)
        );

        verify(httpSecurity, times(1)).authorizeRequests();
        verify(authorizeRequests, times(1)).antMatchers(HttpMethod.POST, "/api/mail/send");
        verify(url, times(1)).permitAll();
        verify(authorizeRequests, times(1)).anyRequest();
        verify(url, times(1)).authenticated();
    }
}