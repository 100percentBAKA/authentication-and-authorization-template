package com.example.security_demo.config;

import java.nio.file.AccessDeniedException;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import com.example.security_demo.dtos.ApiResponseDTO;

@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE) // ! not working
public class GlobalExceptionHandler {

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiResponseDTO<Object>> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex, WebRequest request) {
        ApiResponseDTO<Object> response = new ApiResponseDTO<>(
            "Bad Request",
            ex.getLocalizedMessage(),
            false
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiResponseDTO<Object>> handleAccessDeniedException(AccessDeniedException ex, WebRequest request) {
        ApiResponseDTO<Object> response = new ApiResponseDTO<>(
            "Access Denied: You do not have permission to access this resource", 
            ex.getMessage(), 
            false
        );
        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponseDTO<Object>> handleGlobalException(Exception ex, WebRequest request) {
        ApiResponseDTO<Object> response = new ApiResponseDTO<>(
            "Internal Server Error",
            ex.getLocalizedMessage(),
            false
        );
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
