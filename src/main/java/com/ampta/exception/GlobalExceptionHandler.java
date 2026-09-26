package com.ampta.exception;

import com.ampta.dto.ApiResponse;
import com.ampta.dto.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse<Void>> handleResourceNotFoundException(ResourceNotFoundException ex) {
        log.error("Resource not found exception: {}", ex.getMessage());
        ErrorResponse<Void> response = new ErrorResponse<>(false, ex.getMessage(), null, "404");
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ResourceAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse<Void>> handleResourceAlreadyExistsException(ResourceAlreadyExistsException ex) {
        log.error("Resource already exists exception: {}", ex.getMessage());
        ErrorResponse<Void> response = new ErrorResponse<>(false, ex.getMessage(), null, "409");
        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse<Void>> handleGenericException(Exception ex) {
        log.error("Unhandled exception occurred", ex);
        ErrorResponse<Void> response = new ErrorResponse<>(false, "An unexpected error occurred: " + ex.getMessage(), null, "500");
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
