package com.example.management_demo.exception;

import com.example.management_demo.controller.common.ApiStatus;
import java.time.LocalDateTime;

public record ErrorResponse(
        int code,
        String error,
        String message,
        LocalDateTime timestamp
) {
    public static ErrorResponse of(ApiStatus status, String message) {
        return new ErrorResponse(
                status.getCode(),
                status.getMessage(),
                message,
                LocalDateTime.now()
        );
    }
    
    public static ErrorResponse of(ApiStatus status) {
        return new ErrorResponse(
                status.getCode(),
                status.getMessage(),
                status.getMessage(),
                LocalDateTime.now()
        );
    }
}
