package com.example.management_demo.controller.common;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ApiStatus {
    // Success responses (2xx)
    SUCCESS(200, "Success", HttpStatus.OK),
    CREATED(201, "Created successfully", HttpStatus.CREATED),
    ACCEPTED(202, "Accepted", HttpStatus.ACCEPTED),
    NO_CONTENT(204, "No content", HttpStatus.NO_CONTENT),
    
    // Client errors (4xx)
    BAD_REQUEST(400, "Bad request", HttpStatus.BAD_REQUEST),
    UNAUTHORIZED(401, "Unauthorized", HttpStatus.UNAUTHORIZED),
    FORBIDDEN(403, "Access denied", HttpStatus.FORBIDDEN),
    NOT_FOUND(404, "Resource not found", HttpStatus.NOT_FOUND),
    CONFLICT(409, "Resource conflict", HttpStatus.CONFLICT),
    VALIDATION_ERROR(422, "Validation failed", HttpStatus.valueOf(422)),
    
    // Server errors (5xx)
    INTERNAL_SERVER_ERROR(500, "Internal server error", HttpStatus.INTERNAL_SERVER_ERROR),
    SERVICE_UNAVAILABLE(503, "Service unavailable", HttpStatus.SERVICE_UNAVAILABLE);
    
    private final int code;
    private final String message;
    private final HttpStatus httpStatus;
    
    ApiStatus(int code, String message, HttpStatus httpStatus) {
        this.code = code;
        this.message = message;
        this.httpStatus = httpStatus;
    }
}
