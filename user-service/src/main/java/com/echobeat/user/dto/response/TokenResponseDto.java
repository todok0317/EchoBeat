package com.echobeat.user.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TokenResponseDto {

    private String accessToken;
    private String refreshToken;
    private String tokenType;
    private Long expiresIn;

    public static TokenResponseDto of(String accessToken, String refreshToken) {
        return TokenResponseDto.builder()
            .accessToken(accessToken)
            .refreshToken(refreshToken)
            .tokenType("Bearer")
            .expiresIn(2 * 60 * 60L) // 2시간
            .build();
    }
}
