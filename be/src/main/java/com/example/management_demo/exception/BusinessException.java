package com.example.management_demo.exception;

import com.example.management_demo.controller.common.ApiStatus;
import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException {
    private final ApiStatus status;
    private final String customMessage;
    
    public BusinessException(ApiStatus status, String customMessage) {
        super(customMessage);
        this.status = status;
        this.customMessage = customMessage;
    }
    
    public BusinessException(ApiStatus status) {
        super(status.getMessage());
        this.status = status;
        this.customMessage = status.getMessage();
    }
    
    // methods for common cases
    public static BusinessException unauthorized(String message) {
        return new BusinessException(ApiStatus.UNAUTHORIZED, message);
    }
    
    public static BusinessException forbidden(String message) {
        return new BusinessException(ApiStatus.FORBIDDEN, message);
    }
    
    public static BusinessException notFound(String message) {
        return new BusinessException(ApiStatus.NOT_FOUND, message);
    }
    
    public static BusinessException badRequest(String message) {
        return new BusinessException(ApiStatus.BAD_REQUEST, message);
    }
}
