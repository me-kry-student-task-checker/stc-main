package hu.me.iit.malus.thesis.course.controller;

import hu.me.iit.malus.thesis.course.service.exception.CourseNotFoundException;
import hu.me.iit.malus.thesis.course.service.exception.ForbiddenCourseEdit;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class CourseControllerAdvice {

    private static final String MSG = "msg";

    @ExceptionHandler(CourseNotFoundException.class)
    public ResponseEntity<Map<String, String>> handle(CourseNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(MSG, e.getMessage()));
    }

    @ExceptionHandler(ForbiddenCourseEdit.class)
    public ResponseEntity<Map<String, String>> handle(ForbiddenCourseEdit e) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of(MSG, e.getMessage()));
    }
}
