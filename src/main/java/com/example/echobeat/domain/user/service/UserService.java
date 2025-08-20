package com.example.echobeat.domain.user.service;

import com.example.echobeat.domain.user.dto.request.UserUpdateRequestDto;
import com.example.echobeat.domain.user.dto.response.UserResponseDto;
import com.example.echobeat.domain.user.entity.User;
import com.example.echobeat.domain.user.repository.UserRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // 전체 사용자 조회
    @Transactional(readOnly = true)
    public List<UserResponseDto> getAllUsers() {
        return userRepository.findAll()
            .stream()
            .map(UserResponseDto::from)
            .collect(Collectors.toList());
    }

    // 사용자 ID로 조회
    @Transactional(readOnly = true)
    public UserResponseDto getUserById(Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        return UserResponseDto.from(user);
    }

    // 사용자 정보 수정
    @Transactional
    public UserResponseDto updateUser(Long userId, UserUpdateRequestDto requestDto) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        // 이메일 수정
        if (StringUtils.hasText(requestDto.getEmail())) {
            // 이메일 중복 체크 (본인 제외)
            if (userRepository.existsByEmail(requestDto.getEmail()) && !user.getEmail()
                .equals(requestDto.getEmail())) {
                throw new IllegalArgumentException("이미 사용중인 이메일입니다.");
            }
            user.updateEmail(requestDto.getEmail());
        }
        // 비밀번호 변경
        if (StringUtils.hasText(requestDto.getCurrentPassword()) &&
            StringUtils.hasText(requestDto.getNewPassword())) {

            // 현재 비밀번호 확인
            if (!passwordEncoder.matches(requestDto.getCurrentPassword(), user.getPassword())) {
                throw new IllegalArgumentException("현재 비밀번호가 일치하지 않습니다.");
            }

            String encodedNewPassword = passwordEncoder.encode(requestDto.getNewPassword());
            user.updatePassword(encodedNewPassword);
        }

        User updatedUser = userRepository.save(user);
        return UserResponseDto.from(updatedUser);
    }


}
