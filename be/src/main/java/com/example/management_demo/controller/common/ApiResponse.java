package com.example.management_demo.controller.common;

import java.time.LocalDateTime;

public record ApiResponse<T>(
        int code,
        String message,
        T data,
        LocalDateTime timestamp
) {
    public static <T> ApiResponse<T> of(ApiStatus status, T data) {
        return new ApiResponse<>(status.getCode(), status.getMessage(), data, LocalDateTime.now());
    }
    
    public static <T> ApiResponse<T> of(ApiStatus status, String message, T data) {
        return new ApiResponse<>(status.getCode(), message, data, LocalDateTime.now());
    }
    
    public static <T> ApiResponse<T> success(T data) {
        return of(ApiStatus.SUCCESS, data);
    }
    
    public static <T> ApiResponse<T> success(String message, T data) {
        return of(ApiStatus.SUCCESS, message, data);
    }
    
    public static <T> ApiResponse<T> created(T data) {
        return of(ApiStatus.CREATED, data);
    }
    
    public static <T> ApiResponse<T> created(String message, T data) {
        return of(ApiStatus.CREATED, message, data);
    }
}   
