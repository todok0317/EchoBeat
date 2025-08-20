package com.example.echobeat.domain.user.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserUpdateRequestDto {

    @Email(message = "올바른 이메일 형식을 입력해주세요.")
    private String email;

    private String currentPassword; // 현재 비밀번호 (비밀번호 변경 시)

    private String newPassword; // 새 비밀번호
}