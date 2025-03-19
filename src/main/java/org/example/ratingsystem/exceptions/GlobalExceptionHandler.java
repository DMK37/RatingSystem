package org.example.ratingsystem.exceptions;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({UserAlreadyExistsException.class, InvalidTokenException.class,
            NotPendingStatus.class, InvalidDataException.class})
    public ResponseEntity<Object> handleBadRequest(RuntimeException e) {
        return buildSimpleResponse(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    private static ResponseEntity<Object> buildSimpleResponse(String message, HttpStatus status) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("message", message);
        return new ResponseEntity<>(body, status);
    }

    @ExceptionHandler(MailServiceException.class)
    public ResponseEntity<Object> handleMailServiceException(MailServiceException e) {
        return buildSimpleResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler({LoginFailedException.class})
    public ResponseEntity<Object> handleLoginFailedException(LoginFailedException e) {
        return buildSimpleResponse(e.getMessage(), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<Object> handleExpiredJwtException(ExpiredJwtException e) {
        return buildSimpleResponse("Token has expired", HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler({EntityNotFoundException.class})
    public ResponseEntity<Object> handleNotFoundException(EntityNotFoundException e) {
        return buildSimpleResponse(e.getMessage(), HttpStatus.NOT_FOUND);
    }
}
