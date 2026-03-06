package com.example.management_demo.security;

import com.example.management_demo.controller.common.ApiStatus;
import com.example.management_demo.exception.ErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class SecurityExceptionHandler implements AuthenticationEntryPoint, AccessDeniedHandler {

    private final ObjectMapper objectMapper;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        writeErrorResponse(response, ApiStatus.UNAUTHORIZED, 
                "Authentication required. Please login first.");
    }

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException {
        writeErrorResponse(response, ApiStatus.FORBIDDEN,
                "Access denied. You don't have permission to access this resource.");
    }

    private void writeErrorResponse(HttpServletResponse response, ApiStatus status, String message) 
            throws IOException {
        ErrorResponse errorResponse = ErrorResponse.of(status, message);
        
        response.setStatus(status.getCode());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
    }
}
