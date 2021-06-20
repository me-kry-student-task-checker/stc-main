package hu.me.iit.malus.thesis.email.controller;

import hu.me.iit.malus.thesis.email.service.exception.MailCouldNotBeSentException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class MailControllerAdvice {

    public static final String MSG = "msg";
    public static final String MSG_FORMAT = "%s - { %s }";

    @ExceptionHandler(MailCouldNotBeSentException.class)
    public ResponseEntity<Map<String, String>> handle(MailCouldNotBeSentException e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                Map.of(MSG, String.format(MSG_FORMAT, e.getMessage(), e.getCause().getMessage()))
        );
    }
}
