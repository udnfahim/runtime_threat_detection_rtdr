package com.systemgrade.rtdr.controller;

import com.systemgrade.rtdr.domain.exception.SecuritySystemException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(SecuritySystemException.class)
    public ResponseEntity<String> handleSecuritySystemException(SecuritySystemException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGeneric(Exception ex) {
        // TEMPORARY: Force the actual exception stack trace into stdout/stderr
        System.err.println("=== ACTUATOR ERROR INTERCEPTED ===");
        ex.printStackTrace();
        System.err.println("==================================");

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("internal_error");
    }
}