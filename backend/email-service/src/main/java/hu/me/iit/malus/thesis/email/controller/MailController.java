package hu.me.iit.malus.thesis.email.controller;

import hu.me.iit.malus.thesis.email.controller.dto.MailDto;
import hu.me.iit.malus.thesis.email.service.MailService;
import hu.me.iit.malus.thesis.email.service.exception.MailCouldNotBeSentException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * Mail controller.
 *
 * @author Ilku Krisztián
 */
@RestController
@RequestMapping("/api/mail")
@RequiredArgsConstructor
public class MailController {

    private final MailService mailService;

    @PostMapping("/send")
    public ResponseEntity<Void> send(@Valid @RequestBody MailDto dto) throws MailCouldNotBeSentException {
        mailService.sendEmail(dto);
        return ResponseEntity.ok().build();
    }
}