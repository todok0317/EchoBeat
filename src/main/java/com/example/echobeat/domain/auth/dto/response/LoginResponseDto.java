package com.example.echobeat.domain.auth.dto.response;

import com.example.echobeat.domain.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class LoginResponseDto {

    private String accessToken;

    private String refreshToken;

    private String username;

    private String email;

    private String role;

    public static LoginResponseDto of(String accessToken, String refreshToken, User user) {
        return LoginResponseDto.builder()
            .accessToken(accessToken)
            .refreshToken(refreshToken)
            .username(user.getUsername())
            .email(user.getEmail())
            .role(user.getRole().toString())
            .build();
    }
}
