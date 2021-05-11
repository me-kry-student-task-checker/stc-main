package hu.me.iit.malus.thesis.task.controller;

import hu.me.iit.malus.thesis.task.service.exception.StudentIdNotFoundException;
import hu.me.iit.malus.thesis.task.service.exception.TaskNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class TaskControllerAdvice {

    private static final String MSG = "msg";

    @ExceptionHandler(TaskNotFoundException.class)
    public ResponseEntity<Map<String, String>> handle(TaskNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(MSG, e.getMessage()));
    }

    @ExceptionHandler(StudentIdNotFoundException.class)
    public ResponseEntity<Map<String, String>> handle(StudentIdNotFoundException e) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of(MSG, e.getMessage()));
    }
}
