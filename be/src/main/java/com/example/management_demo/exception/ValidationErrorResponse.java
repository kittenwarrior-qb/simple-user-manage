package com.example.management_demo.exception;

import java.time.LocalDateTime;
import java.util.Map;

public record ValidationErrorResponse(
        int code,
        String error,
        String message,
        Map<String, String> fieldErrors,
        LocalDateTime timestamp
) {
}
