package com.example.echobeat.domain.user.controller;

import com.example.echobeat.domain.user.dto.request.UserUpdateRequestDto;
import com.example.echobeat.domain.user.dto.response.UserResponseDto;
import com.example.echobeat.domain.user.service.UserService;
import com.example.echobeat.common.jwt.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final JwtUtil jwtUtil;

    // 전체 사용자 조회 (관리자만)
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserResponseDto>> getAllUsers() {
        List<UserResponseDto> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    // 내 정보 조회
    @GetMapping("/me")
    public ResponseEntity<UserResponseDto> getMyInfo(HttpServletRequest request) {
        Long userId = getUserIdFromToken(request);
        UserResponseDto user = userService.getUserById(userId);
        return ResponseEntity.ok(user);
    }

    // 특정 사용자 조회 (관리자만)
    @GetMapping("/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserResponseDto> getUserById(@PathVariable Long userId) {
        UserResponseDto user = userService.getUserById(userId);
        return ResponseEntity.ok(user);
    }

    // 내 정보 수정
    @PutMapping("/me")
    public ResponseEntity<UserResponseDto> updateMyInfo(
            @Valid @RequestBody UserUpdateRequestDto requestDto,
            HttpServletRequest request) {
        
        Long userId = getUserIdFromToken(request);
        UserResponseDto updatedUser = userService.updateUser(userId, requestDto);
        return ResponseEntity.ok(updatedUser);
    }

    // 회원 탈퇴
    @DeleteMapping("/me")
    public ResponseEntity<Void> deleteMyAccount(HttpServletRequest request) {
        Long userId = getUserIdFromToken(request);
        userService.deleteUser(userId);
        return ResponseEntity.ok().build();
    }

    // username으로 사용자 조회
    @GetMapping("/search")
    public ResponseEntity<UserResponseDto> getUserByUsername(@RequestParam String username) {
        UserResponseDto user = userService.getUserByUsername(username);
        return ResponseEntity.ok(user);
    }

    // JWT 토큰에서 사용자 ID 추출
    private Long getUserIdFromToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken == null || !bearerToken.startsWith("Bearer ")) {
            throw new IllegalArgumentException("Authorization 헤더가 없거나 형식이 올바르지 않습니다.");
        }
        String token = jwtUtil.substringToken(bearerToken);
        return jwtUtil.getUserIdFromToken(token);
    }
}
