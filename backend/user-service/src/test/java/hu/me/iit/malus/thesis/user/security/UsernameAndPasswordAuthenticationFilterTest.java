package hu.me.iit.malus.thesis.user.security;

import hu.me.iit.malus.thesis.user.security.config.JwtAuthConfig;
import hu.me.iit.malus.thesis.user.service.exception.CannotReadLoginRequestException;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;

import javax.servlet.FilterChain;
import javax.servlet.ReadListener;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class UsernameAndPasswordAuthenticationFilterTest {


    private AuthenticationManager authManager;
    private JwtAuthConfig jwtConfig;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private FilterChain chain;
    private UsernameAndPasswordAuthenticationFilter filter;

    @Before
    public void setUp() {
        authManager = mock(AuthenticationManager.class);
        jwtConfig = mock(JwtAuthConfig.class);
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        chain = mock(FilterChain.class);

        when(jwtConfig.getUri()).thenReturn("/login");
        when(jwtConfig.getSecret()).thenReturn("dummy-secret");
    }

    @Test(expected = CannotReadLoginRequestException.class)
    public void whenInputStreamIsNull_thenAttemptAuthenticationThrowsException() throws IOException {
        // GIVEN
        filter = new UsernameAndPasswordAuthenticationFilter(authManager, jwtConfig);
        when(request.getInputStream()).thenReturn(null);

        // WHEN
        filter.attemptAuthentication(request, response);

        // THEN
    }

    @Test
    public void whenInputStreamContainsCredentials_thenAttemptAuthenticationAuthenticatesUser() throws IOException {
        // GIVEN
        filter = new UsernameAndPasswordAuthenticationFilter(authManager, jwtConfig);
        when(request.getInputStream()).thenAnswer(i -> new ServletInputStream() {
            private final String input = "{\"email\": \"user@example.com\", \"password\":\"pswd\"}";
            private final ByteArrayInputStream inputStream = new ByteArrayInputStream(input.getBytes());
            @Override
            public boolean isFinished() {
                return false;
            }

            @Override
            public boolean isReady() {
                return false;
            }

            @Override
            public void setReadListener(ReadListener readListener) {

            }

            @Override
            public int read() throws IOException {
                return inputStream.read();
            }
        });

        // WHEN
        filter.attemptAuthentication(request, response);

        // THEN
        verify(authManager, times(1)).authenticate(any());
    }

    @Test
    public void successfulAuthentication() throws IOException, ServletException {
        // GIVEN
        PrintWriter printWriter = mock(PrintWriter.class);
        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn("username");
        when(authentication.getAuthorities()).thenReturn(new ArrayList<>());
        when(response.getWriter()).thenReturn(printWriter);
        filter = new UsernameAndPasswordAuthenticationFilter(authManager, jwtConfig);

        // WHEN
        filter.successfulAuthentication(request, response, chain, authentication);

        // THEN
        verify(response, times(1))
                .addHeader(argThat((header) -> header.equals(HttpHeaders.AUTHORIZATION)), any());
    }
}