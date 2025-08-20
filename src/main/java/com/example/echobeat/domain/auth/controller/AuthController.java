package com.example.echobeat.domain.auth.controller;

import com.example.echobeat.common.jwt.JwtUtil;
import com.example.echobeat.domain.auth.dto.request.LoginRequestDto;
import com.example.echobeat.domain.auth.dto.request.SignUpRequestDto;
import com.example.echobeat.domain.auth.dto.response.LoginResponseDto;
import com.example.echobeat.domain.auth.dto.response.TokenResponseDto;
import com.example.echobeat.domain.auth.service.AuthService;
import com.example.echobeat.domain.user.dto.response.UserResponseDto;
import com.example.echobeat.domain.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final JwtUtil jwtUtil;

    // 회원가입
    @PostMapping("/signup")
    public ResponseEntity<UserResponseDto> signup (
        @Valid @RequestBody SignUpRequestDto signUpRequestDto
    ) {
        UserResponseDto responseDto = authService.signup(signUpRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    // 로그인
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login (
        @Valid @RequestBody LoginRequestDto loginRequestDto
    ) {
        LoginResponseDto loginResponseDto = authService.login(loginRequestDto);
        return ResponseEntity.status(HttpStatus.OK).body(loginResponseDto);
    }

    // 토큰 재발급
    @PostMapping("/reissue")
    public ResponseEntity<TokenResponseDto> reissueToken(
        HttpServletRequest request
    ) {
        String refreshToken = extractRefreshTokenFromHeader(request);
        TokenResponseDto responseDto = authService.reissueToken(refreshToken);
        return ResponseEntity.ok(responseDto);
    }

    // 로그아웃
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(
        HttpServletRequest request
    ) {
        String accessToken = extractAccessTokenFromHeader(request);
        Long userId = jwtUtil.getUserIdFromToken(accessToken);
        authService.logout(userId);
        return ResponseEntity.ok().build();
    }

    // Authorization 헤더에서 Access Token 추출
    private String extractAccessTokenFromHeader(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken == null || !bearerToken.startsWith("Bearer ")) {
            throw new IllegalArgumentException("Authorization 헤더가 없거나 형식이 올바르지 않습니다.");
        }
        return jwtUtil.substringToken(bearerToken);
    }

    // 헤더에서 Refresh Token 추출 (보통 별도 헤더나 요청 바디에서)
    private String extractRefreshTokenFromHeader(HttpServletRequest request) {
        String refreshToken = request.getHeader("Refresh-Token");
        if (refreshToken == null) {
            throw new IllegalArgumentException("Refresh-Token 헤더가 없습니다.");
        }
        return refreshToken;
    }

}
