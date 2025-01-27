package cz.xmerta.tennisclub.controller.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import jakarta.validation.ConstraintViolationException;

import java.util.stream.Collectors;

@ControllerAdvice
public class ControllerExceptionHandler {
    /**
     * Occurs in service or controller logic.
     *
     * @param e the thrown IllegalArgumentException
     * @return a ResponseEntity with HTTP 400 status and  message
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }
    /**
     * Handles ConstraintViolationException, which is triggered when a validation constraint
     * (@NotBlank, @Positive, ...) fails in the HTTP request. Each message is formatted into one.
     *
     * @param e the thrown ConstraintViolationException
     * @return a ResponseEntity with HTTP 400 status and  list of  errors
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<String> handleConstraintViolationException(ConstraintViolationException e) {
        String errors = e.getConstraintViolations().stream()
                .map(violation -> violation.getPropertyPath() + ": " + violation.getMessage())
                .collect(Collectors.joining(", "));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }

    /**
     * Handles MethodArgumentNotValidException, which occurs when validation fails for a
     * @Valid annotated object in a controller method (e.g., invalid DTOs).
     * Each message is formatted into one.
     *
     * @param e the thrown MethodArgumentNotValidException
     * @return a ResponseEntity with HTTP 400 status and a list of errors
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        String errors = e.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining(", "));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }
    /**
     * Handles generic exceptions, which are not explicitly handled by other exception handlers.
     * For unexpected errors.
     *
     * @param e the thrown Exception
     * @return a ResponseEntity with HTTP 500 status and a general error message
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGeneralException(Exception e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Internal server error.");
    }
}

