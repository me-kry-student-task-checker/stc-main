package hu.me.iit.malus.thesis.feedback.client.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import hu.me.iit.malus.thesis.feedback.security.config.JwtAuthConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * Custom RequestInterceptor implementation for Feign.
 * It forwards the authentication (JWT) token from the original request,
 * to the called client service.
 * So every service knows, who is the principal behind a request.
 * This bean should be defined in every microservice that possibly calls others.
 * @author Javorek Dénes
 */
@Component
public class JwtForwardingInterceptor implements RequestInterceptor {
    private JwtAuthConfig jwtConfig;

    @Autowired
    public JwtForwardingInterceptor(JwtAuthConfig jwtConfig) {
        this.jwtConfig = jwtConfig;
    }

    @Override
    public void apply(RequestTemplate forwardedRequestTemplate) {
    	RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
    	if (requestAttributes == null) {
    		return;
    	}
        HttpServletRequest originalRequest = ((ServletRequestAttributes) requestAttributes).getRequest();

        String token = originalRequest.getHeader(jwtConfig.getTokenHeader());
        if (token == null || token.length() == 0) {
            return;
        }
        forwardedRequestTemplate.header(jwtConfig.getTokenHeader(), token);
    }
}
