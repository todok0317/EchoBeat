package com.echobeat.user.controller;

import com.echobeat.common.dto.ApiResponse;
import com.echobeat.common.security.CustomUserPrincipal;
import com.echobeat.user.dto.request.UserUpdateRequestDto;
import com.echobeat.user.dto.response.UserResponseDto;
import com.echobeat.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "User", description = "사용자 관련 API")
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @Operation(summary = "내 프로필 조회", description = "현재 로그인한 사용자의 프로필을 조회합니다.")
    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserResponseDto>> getMyProfile(
        @AuthenticationPrincipal CustomUserPrincipal userPrincipal) {
        
        UserResponseDto response = userService.getUserProfile(userPrincipal.getId());
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @Operation(summary = "내 프로필 수정", description = "현재 로그인한 사용자의 프로필을 수정합니다.")
    @PutMapping("/me")
    public ResponseEntity<ApiResponse<UserResponseDto>> updateMyProfile(
        @AuthenticationPrincipal CustomUserPrincipal userPrincipal,
        @Valid @RequestBody UserUpdateRequestDto requestDto) {
        
        UserResponseDto response = userService.updateUserProfile(userPrincipal.getId(), requestDto);
        return ResponseEntity.ok(ApiResponse.success("프로필이 수정되었습니다.", response));
    }

    @Operation(summary = "회원 탈퇴", description = "현재 로그인한 사용자의 계정을 삭제합니다.")
    @DeleteMapping("/me")
    public ResponseEntity<ApiResponse<Void>> deleteMyAccount(
        @AuthenticationPrincipal CustomUserPrincipal userPrincipal) {
        
        userService.deleteUser(userPrincipal.getId());
        return ResponseEntity.ok(ApiResponse.success("회원 탈퇴가 완료되었습니다."));
    }
}
