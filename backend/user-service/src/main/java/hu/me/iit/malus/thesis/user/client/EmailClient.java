package hu.me.iit.malus.thesis.user.client;

import hu.me.iit.malus.thesis.user.client.dto.Mail;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * Feign client class for the Email-service
 * @author Javorek Dénes
 */
@FeignClient(name = "email-service")
public interface EmailClient {
    @PostMapping("/api/mail/send")
    ResponseEntity<String> sendMail(@RequestBody Mail mail);
}