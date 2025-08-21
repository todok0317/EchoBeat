package com.echobeat.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    
    // 공통 에러
    INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST, "C001", "잘못된 입력값입니다."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "C002", "서버 내부 오류가 발생했습니다."),
    
    // 인증/인가 관련
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "A001", "인증이 필요합니다."),
    FORBIDDEN(HttpStatus.FORBIDDEN, "A002", "권한이 없습니다."),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "A003", "유효하지 않은 토큰입니다."),
    EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED, "A004", "만료된 토큰입니다."),
    
    // 사용자 관련
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "U001", "사용자를 찾을 수 없습니다."),
    DUPLICATE_USERNAME(HttpStatus.CONFLICT, "U002", "이미 존재하는 사용자명입니다."),
    INVALID_PASSWORD(HttpStatus.BAD_REQUEST, "U003", "잘못된 비밀번호입니다."),
    
    // 음악 관련
    MUSIC_NOT_FOUND(HttpStatus.NOT_FOUND, "M001", "음악을 찾을 수 없습니다."),
    
    // 즐겨찾기 관련
    FAVORITE_NOT_FOUND(HttpStatus.NOT_FOUND, "F001", "즐겨찾기를 찾을 수 없습니다."),
    ALREADY_FAVORITED(HttpStatus.CONFLICT, "F002", "이미 즐겨찾기에 추가된 항목입니다.");
    
    private final HttpStatus status;
    private final String code;
    private final String message;
}
