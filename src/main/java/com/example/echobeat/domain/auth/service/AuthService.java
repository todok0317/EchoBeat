package com.example.echobeat.domain.auth.service;

import com.example.echobeat.domain.auth.dto.request.SignUpRequestDto;
import com.example.echobeat.domain.user.dto.response.UserResponseDto;
import com.example.echobeat.domain.user.entity.User;
import com.example.echobeat.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;

    // 회원가입
    @Transactional
    public UserResponseDto signup (SignUpRequestDto signUpRequestDto) {
        User user = new User(signUpRequestDto.);
    }
}
