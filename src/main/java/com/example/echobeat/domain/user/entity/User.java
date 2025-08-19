package com.example.echobeat.domain.user.entity;

import com.example.echobeat.domain.auth.dto.request.SignUpRequestDto;
import com.example.echobeat.domain.user.enums.UserRole;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username; // 로그인 ID (또는 email)

    @Column(nullable = false)
    private String password; // 암호화된 비밀번호 (BCrypt)

    @Column(nullable = false, unique = true)
    private String email;

    @Enumerated(EnumType.STRING)
    private UserRole role;

    // 토큰 재발급용
    private String refreshToken;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static User from(String username, String password, String email, UserRole role) {
        return User.builder()
                .username(username)
                .password(password)
                .email(email)
                .role(role != null ? role : UserRole.USER)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }
}
