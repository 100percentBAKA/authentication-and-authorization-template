package com.example.security_demo.dtos;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ApiResponseDTO<T> {
    private boolean success;
    private String message;
    private T data;
    private LocalDateTime timestamp;
    private String error;

    public ApiResponseDTO(String message) {
        this.success = true;
        this.message = message;
        this.data = null;
        this.timestamp = LocalDateTime.now();
    }

    public ApiResponseDTO(String message, T data) {
        this.success = true;
        this.message = message;
        this.data = data;
        this.timestamp = LocalDateTime.now();
    }

    public ApiResponseDTO(String message, String error, boolean success) {
        this.success = false;
        this.message = message;
        this.error = error;
        this.timestamp = LocalDateTime.now();
    }
}
