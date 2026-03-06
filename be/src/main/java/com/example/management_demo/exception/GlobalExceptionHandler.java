package com.example.management_demo.exception;

import com.example.management_demo.controller.common.ApiStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusinessException(BusinessException ex) {
        log.warn("Business exception: {} - {}", ex.getStatus(), ex.getCustomMessage());
        
        ErrorResponse error = ErrorResponse.of(ex.getStatus(), ex.getCustomMessage());
        return ResponseEntity.status(ex.getStatus().getHttpStatus()).body(error);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgument(IllegalArgumentException ex) {
        log.warn("Validation error: {}", ex.getMessage());
        
        ErrorResponse error = ErrorResponse.of(ApiStatus.BAD_REQUEST, ex.getMessage());
        return ResponseEntity.status(ApiStatus.BAD_REQUEST.getHttpStatus()).body(error);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationErrorResponse> handleValidationErrors(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        
        log.warn("Validation failed: {}", errors);
        
        ValidationErrorResponse response = new ValidationErrorResponse(
                ApiStatus.VALIDATION_ERROR.getCode(),
                ApiStatus.VALIDATION_ERROR.getMessage(),
                "Request validation failed. Please check the errors.",
                errors,
                LocalDateTime.now()
        );
        
        return ResponseEntity.status(ApiStatus.VALIDATION_ERROR.getHttpStatus()).body(response);
    }

    @ExceptionHandler(AuthorizationDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAuthorizationDenied(AuthorizationDeniedException ex) {
        log.warn("Access denied: {}", ex.getMessage());
        
        ErrorResponse error = ErrorResponse.of(
                ApiStatus.FORBIDDEN,
                "You don't have permission to access this resource."
        );
        return ResponseEntity.status(ApiStatus.FORBIDDEN.getHttpStatus()).body(error);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
        log.error("Unexpected error occurred: ", ex);
        
        ErrorResponse error = ErrorResponse.of(
                ApiStatus.INTERNAL_SERVER_ERROR,
                "An unexpected error occurred. Please contact support."
        );
        return ResponseEntity.status(ApiStatus.INTERNAL_SERVER_ERROR.getHttpStatus()).body(error);
    }
}
