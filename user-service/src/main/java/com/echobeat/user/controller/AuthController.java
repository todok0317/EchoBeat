package com.echobeat.user.controller;

import com.echobeat.common.dto.ApiResponse;
import com.echobeat.common.security.CustomUserPrincipal;
import com.echobeat.user.dto.request.LoginRequestDto;
import com.echobeat.user.dto.request.SignupRequestDto;
import com.echobeat.user.dto.response.TokenResponseDto;
import com.echobeat.user.dto.response.UserResponseDto;
import com.echobeat.user.service.AuthService;
import com.echobeat.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Auth", description = "인증 관련 API")
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final UserService userService;

    @Operation(summary = "회원가입", description = "새 사용자를 등록합니다.")
    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<UserResponseDto>> signup(@Valid @RequestBody SignupRequestDto requestDto) {
        UserResponseDto response = userService.signup(requestDto);
        return ResponseEntity.ok(ApiResponse.success("회원가입이 완료되었습니다.", response));
    }

    @Operation(summary = "로그인", description = "사용자 로그인을 처리합니다.")
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<TokenResponseDto>> login(@Valid @RequestBody LoginRequestDto requestDto) {
        TokenResponseDto response = authService.login(requestDto);
        return ResponseEntity.ok(ApiResponse.success("로그인이 완료되었습니다.", response));
    }

    @Operation(summary = "토큰 갱신", description = "Access Token을 갱신합니다.")
    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<TokenResponseDto>> refreshToken(HttpServletRequest request) {
        String refreshToken = extractRefreshToken(request);
        TokenResponseDto response = authService.refreshToken(refreshToken);
        return ResponseEntity.ok(ApiResponse.success("토큰이 갱신되었습니다.", response));
    }

    @Operation(summary = "로그아웃", description = "사용자 로그아웃을 처리합니다.")
    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout(@AuthenticationPrincipal CustomUserPrincipal userPrincipal) {
        authService.logout(userPrincipal.getId());
        return ResponseEntity.ok(ApiResponse.success("로그아웃이 완료되었습니다."));
    }

    private String extractRefreshToken(HttpServletRequest request) {
        String refreshToken = request.getHeader("Refresh-Token");
        if (refreshToken == null) {
            throw new IllegalArgumentException("Refresh Token이 필요합니다.");
        }
        return refreshToken;
    }
}
