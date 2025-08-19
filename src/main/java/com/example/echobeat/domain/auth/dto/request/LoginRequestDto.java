package com.example.echobeat.domain.auth.dto.request;

import com.example.echobeat.domain.user.enums.UserRole;

public class LoginRequestDto {

    private String username; // 로그인 ID (또는 email)

    private String password; // 암호화된 비밀번호 (BCrypt)

    private String email;

    private UserRole role;

}
