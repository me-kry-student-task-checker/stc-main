package hu.me.iit.malus.thesis.user.security;

import com.netflix.ribbon.proxy.annotation.Http;
import hu.me.iit.malus.thesis.user.security.config.JwtAuthConfig;
import org.junit.Before;
import org.junit.Test;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class JwtAuthorizationFilterTest {

    private final String tokenHeader = "Authorization";

    private HttpServletRequest request;
    private HttpServletResponse response;
    private JwtAuthConfig jwtAuthConfig;
    private FilterChain chain;

    @Before
    public void setUp() {
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        jwtAuthConfig = mock(JwtAuthConfig.class);
        chain = mock(FilterChain.class);
    }

    @Test
    public void whenRequestHasNoToken_doFilterInternalChainsRequest() throws ServletException, IOException {
        // GIVEN
        when(jwtAuthConfig.getTokenHeader()).thenReturn(tokenHeader);
        when(request.getHeader(tokenHeader)).thenReturn(null);

        JwtAuthorizationFilter filter = new JwtAuthorizationFilter(jwtAuthConfig);

        // WHEN
        filter.doFilterInternal(request, response, chain);

        // THEN
        verify(request, times(1)).getHeader(tokenHeader);
        verify(chain, times(1)).doFilter(request, response);
    }

    @Test
    public void whenRequestHasAToken_doFilterInternalChainsRequest() throws ServletException, IOException {
        // GIVEN
        when(jwtAuthConfig.getSecret()).thenReturn("dummy-secret");
        when(jwtAuthConfig.getTokenHeader()).thenReturn(tokenHeader);
        when(request.getHeader(tokenHeader)).thenReturn("dummy-token");

        JwtAuthorizationFilter filter = new JwtAuthorizationFilter(jwtAuthConfig);

        // WHEN
        filter.doFilterInternal(request, response, chain);

        // THEN
        verify(request, times(1)).getHeader(tokenHeader);
        verify(chain, times(1)).doFilter(request, response);
    }
}