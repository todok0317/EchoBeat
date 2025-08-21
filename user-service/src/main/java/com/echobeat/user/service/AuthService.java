package com.echobeat.user.service;

import com.echobeat.common.exception.BusinessException;
import com.echobeat.common.exception.ErrorCode;
import com.echobeat.common.jwt.JwtUtil;
import com.echobeat.user.dto.request.LoginRequestDto;
import com.echobeat.user.dto.response.TokenResponseDto;
import com.echobeat.user.entity.User;
import com.echobeat.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public TokenResponseDto login(LoginRequestDto requestDto) {
        // 사용자 조회
        User user = userRepository.findByUsername(requestDto.getUsername())
            .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        // 비밀번호 검증
        if (!passwordEncoder.matches(requestDto.getPassword(), user.getPassword())) {
            throw new BusinessException(ErrorCode.INVALID_PASSWORD);
        }

        // 토큰 생성
        String accessToken = jwtUtil.createAccessToken(user.getId(), user.getUsername(), user.getRole().name());
        String refreshToken = jwtUtil.createRefreshToken(user.getId());

        log.info("사용자 로그인 성공: {}", user.getUsername());
        
        return TokenResponseDto.of(accessToken, refreshToken);
    }

    public TokenResponseDto refreshToken(String refreshToken) {
        // Refresh Token 검증
        if (!jwtUtil.validateRefreshToken(refreshToken)) {
            throw new BusinessException(ErrorCode.INVALID_TOKEN, "유효하지 않은 Refresh Token입니다.");
        }

        // 사용자 정보 조회
        Long userId = jwtUtil.getUserIdFromToken(refreshToken);
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        // 새 토큰 생성
        String newAccessToken = jwtUtil.createAccessToken(user.getId(), user.getUsername(), user.getRole().name());
        String newRefreshToken = jwtUtil.createRefreshToken(user.getId());

        log.info("토큰 갱신: {}", user.getUsername());
        
        return TokenResponseDto.of(newAccessToken, newRefreshToken);
    }

    public void logout(Long userId) {
        // Redis에서 Refresh Token 삭제
        jwtUtil.deleteRefreshTokenFromRedis(userId);
        log.info("사용자 로그아웃: userId={}", userId);
    }
}
