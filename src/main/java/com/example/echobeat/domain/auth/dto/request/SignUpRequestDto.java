package com.example.echobeat.domain.auth.dto.request;

import com.example.echobeat.domain.user.enums.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SignUpRequestDto {

    @NotBlank(message = "아이디는 필수 입력 값입니다.")
    private String username; // 로그인 ID

    @NotBlank(message = "비밀번호는 필수 입력 값입니다.")
    private String password; // 비밀번호

    @Email(message = "올바른 이메일 형식을 입력해주세요.")
    @NotBlank(message = "이메일은 필수 입력 값입니다.")
    private String email;

    private UserRole role = UserRole.USER; // 기본값 USER

}
