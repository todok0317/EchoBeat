package com.echobeat.common.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ApiResponse<T> {
    
    private final boolean success;
    private final String message;
    private final T data;
    
    public static <T> ApiResponse<T> success(T data) {
        return ApiResponse.<T>builder()
            .success(true)
            .message("성공")
            .data(data)
            .build();
    }
    
    public static <T> ApiResponse<T> success(String message, T data) {
        return ApiResponse.<T>builder()
            .success(true)
            .message(message)
            .data(data)
            .build();
    }
    
    public static ApiResponse<Void> success(String message) {
        return ApiResponse.<Void>builder()
            .success(true)
            .message(message)
            .build();
    }
    
    public static <T> ApiResponse<T> error(String message) {
        return ApiResponse.<T>builder()
            .success(false)
            .message(message)
            .build();
    }
}
