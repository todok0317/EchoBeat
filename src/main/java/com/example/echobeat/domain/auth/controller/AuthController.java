package com.example.echobeat.domain.auth.controller;

import com.example.echobeat.domain.auth.dto.request.SignUpRequestDto;
import com.example.echobeat.domain.auth.service.AuthService;
import com.example.echobeat.domain.user.dto.response.UserResponseDto;
import com.example.echobeat.domain.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final AuthService authService;

    // 회원가입
    @PostMapping("/signup")
    public ResponseEntity<UserResponseDto> signup (
        @Valid @RequestBody SignUpRequestDto signUpRequestDto
    ) {
        UserResponseDto responseDto = authService.signup(signUpRequestDto);
        return ResponseEntity.ok(responseDto);
    }

    // 로그인


    // 토큰 재발급

}
