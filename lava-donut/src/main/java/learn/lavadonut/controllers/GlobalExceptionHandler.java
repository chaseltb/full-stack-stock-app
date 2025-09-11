package learn.lavadonut.controllers;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    // catch all handler
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception ex) {
        return new ResponseEntity<>(
                new ErrorResponse("Sorry, something unexpected went wrong"),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    // data integrity violation exception handler
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
        return new ResponseEntity<>(
                new ErrorResponse("Data integrity violation. Please check your submission"),
                HttpStatus.BAD_REQUEST
        );
    }

    // data access exception handler (parent class of DataIntegrityViolationException)
    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<ErrorResponse> handleDataAccessException(DataAccessException ex) {
        return new ResponseEntity<>(
                new ErrorResponse("There was a problem accessing data"),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    // HTTP media type not supported exception handler
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<ErrorResponse> handleHttpMediaTypeNotSupportedException(HttpMediaTypeNotSupportedException ex) {
        return new ResponseEntity<>(
                new ErrorResponse("Unsupported media type"),
                HttpStatus.UNSUPPORTED_MEDIA_TYPE
        );
    }

    // HTTP message not readable exception handler
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleHttpMediaTypeNotSupportedException(HttpMessageNotReadableException ex) {
        return new ResponseEntity<>(
                new ErrorResponse("Message not readable"),
                HttpStatus.BAD_REQUEST
        );
    }
}

