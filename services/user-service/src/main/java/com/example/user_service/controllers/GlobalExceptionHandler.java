package com.example.user_service.controllers;

import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.security.SignatureException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * GlobalExceptionHandler class that handles exceptions globally in the application.
 * It provides custom responses for specific exceptions.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles UsernameNotFoundException and BadCredentialsException.
     * Returns a 401 Unauthorized response with a custom error message.
     *
     * @param e The exception that was thrown.
     * @return A ResponseEntity with a 401 status and an error message.
     */
    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<?> handleUsernameNotFound(UsernameNotFoundException e) {
        Map<String, String> error = new HashMap<>();
        error.put("error", "Invalid username or password");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
    }

    /**
     * Handles BadCredentialsException.
     * Returns a 401 Unauthorized response with a custom error message.
     *
     * @param e The exception that was thrown.
     * @return A ResponseEntity with a 401 status and an error message.
     */
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<?> handleBadCredentials(BadCredentialsException e) {
        Map<String, String> error = new HashMap<>();
        error.put("error", "Invalid username or password");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
    }

    /**
     * Handles validation errors from @Valid annotations.
     *
     * @param e The exception thrown during validation
     * @return The error response with field errors
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException e) {
        Map<String, String> errors = new HashMap<>();
        BindingResult bindingResult = e.getBindingResult();
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();

        for (FieldError fieldError : fieldErrors) {
            errors.put(fieldError.getField(), fieldError.getDefaultMessage());
        }

        return errors;
    }

    /**
     * Handles custom validation errors like @NotBlank, @Size etc.
     *
     * @param ex The exception thrown for validation
     * @return The error response with custom validation messages
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Map<String, String>> handleConstraintViolationException(ConstraintViolationException ex) {
        Map<String, String> errors = new HashMap<>();

        ex.getConstraintViolations().forEach(violation ->
                errors.put(violation.getPropertyPath().toString(), violation.getMessage()));

        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles SignatureException.
     * Returns a 401 Unauthorized response with a custom error message.
     *
     * @param e The exception that was thrown.
     * @return A ResponseEntity with a 401 status and an error message.
     */
    @ExceptionHandler(SignatureException.class)
    public ResponseEntity<Map<String, String>> handleInvalidJwtSignature(SignatureException e) {
        Map<String, String> error = new HashMap<>();
        error.put("error", "Invalid or tampered JWT token.");
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .contentType(MediaType.APPLICATION_JSON)
                .body(error);
    }


    /**
     * Handles all other exceptions.
     * Returns a 500 Internal Server Error response with a generic error message.
     *
     * @param e The exception that was thrown.
     * @return A ResponseEntity with a 500 status and a generic error message.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleOtherExceptions(Exception e) {
        Map<String, String> error = new HashMap<>();
        error.put("error", e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
}
