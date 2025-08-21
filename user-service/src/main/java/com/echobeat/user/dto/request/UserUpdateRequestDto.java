package com.echobeat.user.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserUpdateRequestDto {

    @Email(message = "올바른 이메일 형식이 아닙니다.")
    private String email;

    @Size(max = 50, message = "닉네임은 50자 이하여야 합니다.")
    private String nickname;
}
