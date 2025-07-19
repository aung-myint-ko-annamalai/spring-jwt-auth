package com.ucsy.springjwtauth.exception;

import com.ucsy.springjwtauth.dtos.ErrorMessage;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorMessage> handleValidationException(MethodArgumentNotValidException ex) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        Map<String, String> fieldErrors = new HashMap<>();

        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            fieldErrors.put(fieldError.getField(), fieldError.getDefaultMessage());
        }

        ErrorMessage error = ErrorMessage.builder().status(status).
                message(fieldErrors).timestamp(LocalDateTime.now()).build();

        return ResponseEntity.status(status).body(error);

    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorMessage> handleAllExceptions(Exception ex) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        ErrorMessage error = ErrorMessage.builder()
                .status(status)
                .message(Map.of("error", ex.getMessage()))
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.status(status).body(error);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorMessage> handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        ErrorMessage error = ErrorMessage.builder()
                .status(status)
                .message(Map.of("error", "Data integrity violation is found"))
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.status(status).body(error);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorMessage> handleNotFoundException(NotFoundException ex) {
        HttpStatus status = HttpStatus.NOT_FOUND;
        Map<String, String> message = new HashMap<>();
        message.put("error", ex.getMessage());

        ErrorMessage error = ErrorMessage.builder().status(status).
                message(message).timestamp(LocalDateTime.now()).build();

        return ResponseEntity.status(status).body(error);
    }

    @ExceptionHandler(GeneralException.class)
    public ResponseEntity<ErrorMessage> handleGeneralException(GeneralException ex) {
        HttpStatus status = HttpStatus.CONFLICT;
        Map<String, String> message = new HashMap<>();
        message.put("error", ex.getMessage());

        ErrorMessage error = ErrorMessage.builder().status(status).
                message(message).timestamp(LocalDateTime.now()).build();

        return ResponseEntity.status(status).body(error);
    }

}
