package hu.me.iit.malus.thesis.user.controller;

import hu.me.iit.malus.thesis.user.service.exception.DatabaseOperationFailedException;
import hu.me.iit.malus.thesis.user.service.exception.EmailExistsException;
import hu.me.iit.malus.thesis.user.service.exception.UserAlreadyExistException;
import hu.me.iit.malus.thesis.user.service.exception.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class UserControllerAdvice {

    private static final String MSG = "msg";

    @ExceptionHandler(EmailExistsException.class)
    public ResponseEntity<Map<String, String>> handle(EmailExistsException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(MSG, e.getMessage()));
    }

    @ExceptionHandler(UserAlreadyExistException.class)
    public ResponseEntity<Map<String, String>> handle(UserAlreadyExistException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(MSG, e.getMessage()));
    }

    @ExceptionHandler(DatabaseOperationFailedException.class)
    public ResponseEntity<Map<String, String>> handle(DatabaseOperationFailedException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(MSG, e.getCause().getMessage()));
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Map<String, String>> handle(UserNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(MSG, e.getMessage()));
    }
}
