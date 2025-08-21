package com.example.echobeat.domain.auth.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class LoginRequestDto {

    @NotBlank(message = "아이디는 필수 입력 값입니다.")
    private String username; // 로그인 ID

    @NotBlank(message = "비밀번호는 필수 입력 값입니다.")
    private String password; // 비밀번호

}
