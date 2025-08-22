package com.echobeat.user.service;

import com.echobeat.common.exception.BusinessException;
import com.echobeat.common.exception.ErrorCode;
import com.echobeat.user.dto.request.SignupRequestDto;
import com.echobeat.user.dto.request.UserUpdateRequestDto;
import com.echobeat.user.dto.response.UserResponseDto;
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
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserResponseDto signup(SignupRequestDto requestDto) {
        // 중복 검사
        if (userRepository.existsByUsername(requestDto.getUsername())) {
            throw new BusinessException(ErrorCode.DUPLICATE_USERNAME);
        }
        
        if (userRepository.existsByEmail(requestDto.getEmail())) {
            throw new BusinessException(ErrorCode.DUPLICATE_USERNAME, "이미 존재하는 이메일입니다.");
        }

        // 사용자 생성
        User user = User.create(
            requestDto.getUsername(),
            passwordEncoder.encode(requestDto.getPassword()),
            requestDto.getEmail(),
            requestDto.getNickname()
        );

        User savedUser = userRepository.save(user);
        log.info("새 사용자 등록: {}", savedUser.getUsername());

        return UserResponseDto.from(savedUser);
    }

    @Transactional(readOnly = true)
    public UserResponseDto getUserProfile(Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
        
        return UserResponseDto.from(user);
    }

    public UserResponseDto updateUserProfile(Long userId, UserUpdateRequestDto requestDto) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        // 이메일 중복 체크 (자신 제외)
        if (requestDto.getEmail() != null && !user.getEmail().equals(requestDto.getEmail())) {
            if (userRepository.existsByEmail(requestDto.getEmail())) {
                throw new BusinessException(ErrorCode.DUPLICATE_USERNAME, "이미 존재하는 이메일입니다.");
            }
        }

        user.updateProfile(
            requestDto.getEmail() != null ? requestDto.getEmail() : user.getEmail(),
            requestDto.getNickname() != null ? requestDto.getNickname() : user.getNickname()
        );

        log.info("사용자 프로필 업데이트: {}", user.getUsername());
        return UserResponseDto.from(user);
    }

    public void deleteUser(Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        userRepository.delete(user);
        log.info("사용자 삭제: {}", user.getUsername());
    }
}
