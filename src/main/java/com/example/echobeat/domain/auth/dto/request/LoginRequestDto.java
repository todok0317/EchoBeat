package com.example.echobeat.domain.auth.dto.request;

import com.example.echobeat.domain.user.enums.UserRole;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class LoginRequestDto {

    @NotBlank(message = "아이디는 필수 입력 값입니다.")
    private String username; // 로그인 ID

    @NotBlank(message = "비밀번호는 필수 입력 값입니다.")
    private String password; // 암호화된 비밀번호 (BCrypt)

}
