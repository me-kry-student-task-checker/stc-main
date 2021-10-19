package hu.me.iit.malus.thesis.email.security;

import hu.me.iit.malus.thesis.email.security.config.JwtAuthConfig;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class JwtAuthorizationFilterTest {

    @Mock
    HttpServletRequest request;

    @Mock
    HttpServletResponse response;

    @Mock
    FilterChain chain;

    @Mock
    JwtAuthConfig jwtAuthConfig;

    @Captor
    ArgumentCaptor<Authentication> authCaptor;

    @InjectMocks
    JwtAuthorizationFilter jwtAuthorizationFilter;

    @Test
    public void doFilterInternalWithNullToken() throws IOException, ServletException {
        try(MockedStatic<SecurityContextHolder> securityContextHolder = mockStatic(SecurityContextHolder.class)) {
            String tokenHeader = "token";
            String headerValue = null;

            when(jwtAuthConfig.getTokenHeader()).thenReturn(tokenHeader);
            when(request.getHeader(any(String.class))).thenReturn(headerValue);

            this.jwtAuthorizationFilter.doFilterInternal(request, response, chain);

            verify(jwtAuthConfig, times(1)).getTokenHeader();
            verify(request, times(1)).getHeader(tokenHeader);
            securityContextHolder.verify(SecurityContextHolder::clearContext, times(1));
            verify(chain, times(1)).doFilter(request, response);
        }
    }

    @Test
    public void doFilterInternalWithEmptyToken() throws IOException, ServletException {
        try(MockedStatic<SecurityContextHolder> securityContextHolder = mockStatic(SecurityContextHolder.class)) {
            String tokenHeader = "token";
            String headerValue = "";

            when(jwtAuthConfig.getTokenHeader()).thenReturn(tokenHeader);
            when(request.getHeader(any(String.class))).thenReturn(headerValue);

            this.jwtAuthorizationFilter.doFilterInternal(request, response, chain);

            verify(jwtAuthConfig, times(1)).getTokenHeader();
            verify(request, times(1)).getHeader(tokenHeader);
            securityContextHolder.verify(SecurityContextHolder::clearContext, times(1));
            verify(chain, times(1)).doFilter(request, response);
        }
    }

    @Test
    public void doFilterInternalWithInvalidToken() throws IOException, ServletException {
        try(MockedStatic<SecurityContextHolder> securityContextHolder = mockStatic(SecurityContextHolder.class)) {
            String header = "token";
            String secret = "secret";
            String token = "invalid";

            when(jwtAuthConfig.getTokenHeader()).thenReturn(header);
            when(jwtAuthConfig.getSecret()).thenReturn(secret);
            when(request.getHeader(any(String.class))).thenReturn(token);

            this.jwtAuthorizationFilter.doFilterInternal(request, response, chain);

            verify(jwtAuthConfig, times(1)).getTokenHeader();
            verify(request, times(1)).getHeader(header);
            securityContextHolder.verify(SecurityContextHolder::clearContext, times(1));
            verify(chain, times(1)).doFilter(request, response);
        }
    }

    @Test
    public void doFilterInternalWithValidTokenNoRoles() throws IOException, ServletException {
        SecurityContext securityContext = mock(SecurityContext.class);
        try(MockedStatic<SecurityContextHolder> securityContextHolder = mockStatic(SecurityContextHolder.class)) {
            String header = "token";
            String secret = "qwertyuiopasdfghjklzxcvbnm123456";
            String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJpc3MiOiJUZXN0ZXIiLCJpYXQiOjE2MzQ2NTIyMTIsImV4cCI6MzMxOTE1NjEwMTIsImF1ZCI6Ind3dy50ZXN0LmNvbSIsInN1YiI6InRlc3Qtc3ViamVjdEBlbWFpbC5jb20iLCJHaXZlbk5hbWUiOiJUZXN0R2l2ZW5OYW1lIiwiU3VybmFtZSI6IlRlc3RTdXJuYW1lIiwiRW1haWwiOiJ0ZXN0QGVtYWlsLmNvbSJ9.DL088GYndOmMUXxzyXURt_83WfudzSx1vHXgS4t5sfu_0cE8RICULIRR74-jKVcdx-IMpDzuZm__LMrawKeEnQ";
            String subject = "test-subject@email.com";
            String[] roles = new String[]{};

            when(jwtAuthConfig.getTokenHeader()).thenReturn(header);
            when(jwtAuthConfig.getSecret()).thenReturn(secret);
            when(request.getHeader(any(String.class))).thenReturn(token);
            securityContextHolder.when(SecurityContextHolder::getContext).thenReturn(securityContext);

            this.jwtAuthorizationFilter.doFilterInternal(request, response, chain);

            verify(jwtAuthConfig, times(1)).getTokenHeader();
            verify(request, times(1)).getHeader(header);

            securityContextHolder.verify(SecurityContextHolder::getContext, times(1));

            verify(securityContext, times(1)).setAuthentication(authCaptor.capture());
            Assert.assertNotNull(authCaptor.getValue());
            Assert.assertTrue(authCaptor.getValue() instanceof UsernamePasswordAuthenticationToken);
            Assert.assertNotNull(authCaptor.getValue().getPrincipal());
            Assert.assertTrue(authCaptor.getValue().getPrincipal() instanceof String);
            Assert.assertEquals(subject, authCaptor.getValue().getPrincipal());
            Assert.assertNull(authCaptor.getValue().getCredentials());
            Assert.assertNotNull(authCaptor.getValue().getAuthorities());
            Assert.assertEquals(roles.length, authCaptor.getValue().getAuthorities().size());

            Object[] gotRoles = authCaptor.getValue().getAuthorities().toArray();
            for (int i = 0; i < roles.length; i++) {
                Assert.assertTrue(gotRoles[i] instanceof SimpleGrantedAuthority);
                Assert.assertEquals(roles[i], ((SimpleGrantedAuthority)gotRoles[i]).getAuthority());
            }

            securityContextHolder.verify(SecurityContextHolder::clearContext, times(0));
            verify(chain, times(1)).doFilter(request, response);
        }
    }

    @Test
    public void doFilterInternalWithValidTokenHasRoles() throws IOException, ServletException {
        SecurityContext securityContext = mock(SecurityContext.class);
        try(MockedStatic<SecurityContextHolder> securityContextHolder = mockStatic(SecurityContextHolder.class)) {
            String header = "token";
            String secret = "qwertyuiopasdfghjklzxcvbnm123456";
            String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJpc3MiOiJUZXN0ZXIiLCJpYXQiOjE2MzQ2NTIyMTIsImV4cCI6MzMxOTE1NjEwMTIsImF1ZCI6Ind3dy50ZXN0LmNvbSIsInN1YiI6InRlc3Qtc3ViamVjdEBlbWFpbC5jb20iLCJHaXZlbk5hbWUiOiJUZXN0R2l2ZW5OYW1lIiwiU3VybmFtZSI6IlRlc3RTdXJuYW1lIiwiRW1haWwiOiJ0ZXN0QGVtYWlsLmNvbSIsInJvbGVzIjpbIlJPTEUxIiwiUk9MRTIiXX0.LGy0psmhahjko_93nfpTX2YB-fyjywHEJUNtJw7sXnEI3bQ5RShZa2YIVBQuNrPtt8zG11QGNvEEJgS7yOP8kQ";
            String subject = "test-subject@email.com";
            String[] roles = new String[]{"ROLE1", "ROLE2"};

            when(jwtAuthConfig.getTokenHeader()).thenReturn(header);
            when(jwtAuthConfig.getSecret()).thenReturn(secret);
            when(request.getHeader(any(String.class))).thenReturn(token);
            securityContextHolder.when(SecurityContextHolder::getContext).thenReturn(securityContext);

            this.jwtAuthorizationFilter.doFilterInternal(request, response, chain);

            verify(jwtAuthConfig, times(1)).getTokenHeader();
            verify(request, times(1)).getHeader(header);

            securityContextHolder.verify(SecurityContextHolder::getContext, times(1));

            verify(securityContext, times(1)).setAuthentication(authCaptor.capture());
            Assert.assertNotNull(authCaptor.getValue());
            Assert.assertTrue(authCaptor.getValue() instanceof UsernamePasswordAuthenticationToken);
            Assert.assertNotNull(authCaptor.getValue().getPrincipal());
            Assert.assertTrue(authCaptor.getValue().getPrincipal() instanceof String);
            Assert.assertEquals(subject, authCaptor.getValue().getPrincipal());
            Assert.assertNull(authCaptor.getValue().getCredentials());
            Assert.assertNotNull(authCaptor.getValue().getAuthorities());
            Assert.assertEquals(roles.length, authCaptor.getValue().getAuthorities().size());

            Object[] gotRoles = authCaptor.getValue().getAuthorities().toArray();
            for (int i = 0; i < roles.length; i++) {
                Assert.assertTrue(gotRoles[i] instanceof SimpleGrantedAuthority);
                Assert.assertEquals(roles[i], ((SimpleGrantedAuthority)gotRoles[i]).getAuthority());
            }

            securityContextHolder.verify(SecurityContextHolder::clearContext, times(0));
            verify(chain, times(1)).doFilter(request, response);
        }
    }
}