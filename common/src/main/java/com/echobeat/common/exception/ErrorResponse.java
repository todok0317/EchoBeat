package com.echobeat.common.exception;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ErrorResponse {
    
    private final String code;
    private final String message;
    private final Object data;
    
    public static ErrorResponse of(ErrorCode errorCode) {
        return ErrorResponse.builder()
            .code(errorCode.getCode())
            .message(errorCode.getMessage())
            .build();
    }
    
    public static ErrorResponse of(ErrorCode errorCode, Object data) {
        return ErrorResponse.builder()
            .code(errorCode.getCode())
            .message(errorCode.getMessage())
            .data(data)
            .build();
    }
}
