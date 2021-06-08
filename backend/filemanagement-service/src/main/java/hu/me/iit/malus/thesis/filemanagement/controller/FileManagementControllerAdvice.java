package hu.me.iit.malus.thesis.filemanagement.controller;

import hu.me.iit.malus.thesis.filemanagement.service.exceptions.FileNotFoundException;
import hu.me.iit.malus.thesis.filemanagement.service.exceptions.ForbiddenFileDeleteException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.IOException;
import java.util.Map;

@RestControllerAdvice
public class FileManagementControllerAdvice {

    private static final String MSG = "msg";

    @ExceptionHandler(IOException.class)
    public ResponseEntity<Map<String, String>> handle(IOException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(MSG, e.getMessage()));
    }

    @ExceptionHandler(FileNotFoundException.class)
    public ResponseEntity<Map<String, String>> handle(FileNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(MSG, e.getMessage()));
    }

    @ExceptionHandler(ForbiddenFileDeleteException.class)
    public ResponseEntity<Map<String, String>> handle(ForbiddenFileDeleteException e) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of(MSG, e.getMessage()));
    }
}
