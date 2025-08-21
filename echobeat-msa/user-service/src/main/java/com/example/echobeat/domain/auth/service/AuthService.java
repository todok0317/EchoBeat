package com.example.echobeat.domain.auth.service;

import com.example.echobeat.common.jwt.JwtUtil;
import com.example.echobeat.domain.auth.dto.request.LoginRequestDto;
import com.example.echobeat.domain.auth.dto.request.SignUpRequestDto;
import com.example.echobeat.domain.auth.dto.response.LoginResponseDto;
import com.example.echobeat.domain.auth.dto.response.TokenResponseDto;
import com.example.echobeat.domain.user.dto.response.UserResponseDto;
import com.example.echobeat.domain.user.entity.User;
import com.example.echobeat.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    // 회원가입
    @Transactional
    public UserResponseDto signup (SignUpRequestDto signUpRequestDto) {

        // username 중복 체크
        if (userRepository.existsByUsername(signUpRequestDto.getUsername())) {
            throw new IllegalArgumentException("이미 존재하는 아이디입니다.");
        }

        // 이메일 중복 체크 추가
        if (userRepository.existsByEmail(signUpRequestDto.getEmail())) {
            throw new IllegalArgumentException("이미 존재하는 이메일입니다.");
        }

        // 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(signUpRequestDto.getPassword());

        User user = User.from(signUpRequestDto.getUsername(), encodedPassword, signUpRequestDto.getEmail(), signUpRequestDto.getRole());

        User savedUser = userRepository.save(user);

        return UserResponseDto.from(savedUser);
    }

    // 로그인
    @Transactional
    public LoginResponseDto login (LoginRequestDto requestDto) {
        User user = userRepository.findByUsername(requestDto.getUsername())
            .orElseThrow(()-> new IllegalArgumentException("존재하지 않는 아이디입니다."));

        if(!passwordEncoder.matches(requestDto.getPassword(), user.getPassword())){
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        String accessToken = jwtUtil.createAccessToken(user.getId(), user.getUsername(), user.getRole().toString());
        String refreshToken = jwtUtil.createRefreshToken(user.getId());

        return LoginResponseDto.of(accessToken, refreshToken, user);
    }

    // 토큰 재발급
    @Transactional(readOnly = true)
    public TokenResponseDto reissueToken (String refreshToken) {
        // JwtUtil에서 Refresh Token 검증 (JWT + Redis)
        if(!jwtUtil.validateRefreshToken(refreshToken)) {
            throw new IllegalArgumentException("유효하지 않는 Refresh Token입니다.");
        }

        Long userId = jwtUtil.getUserIdFromToken(refreshToken);

        // 사용자 정보 조회
        User user = userRepository.findById(userId)
            .orElseThrow(()-> new IllegalArgumentException("존재하지 않은 사용자입니다."));

        // 새로운 토큰 생성 (JwtUtil이 Redis 저장까지 처리)
        String newAccessToken = jwtUtil.createAccessToken(user.getId(), user.getUsername(), user.getRole().toString());
        String newRefreshToken = jwtUtil.createRefreshToken(user.getId());

        return TokenResponseDto.of(newAccessToken, newRefreshToken);
    }

    // 로그아웃
    @Transactional
    public void logout(Long userId) {
        jwtUtil.deleteRefreshTokenFromRedis(userId);
    }
}
