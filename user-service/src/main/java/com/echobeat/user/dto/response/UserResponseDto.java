package com.echobeat.user.dto.response;

import com.echobeat.user.entity.User;
import com.echobeat.user.enums.UserRole;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class UserResponseDto {

    private Long id;
    private String username;
    private String email;
    private String nickname;
    private UserRole role;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static UserResponseDto from(User user) {
        return UserResponseDto.builder()
            .id(user.getId())
            .username(user.getUsername())
            .email(user.getEmail())
            .nickname(user.getNickname())
            .role(user.getRole())
            .createdAt(user.getCreatedAt())
            .updatedAt(user.getUpdatedAt())
            .build();
    }
}
