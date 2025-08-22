package com.echobeat.common.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Slf4j(topic = "JwtUtil")
@Component
@RequiredArgsConstructor
public class JwtUtil {

    private static final String BEARER_PREFIX = "Bearer ";
    private static final long TOKEN_TIME = 2 * 60 * 60 * 1000L; // 2시간
    private static final long REFRESH_TOKEN_TIME = 7 * 24 * 60 * 60 * 1000L; // 7일
    private static final long REFRESH_TOKEN_EXPIRE_DAYS = 7; // Redis 저장용

    @Value("${jwt.secret.key}")
    private String secretKey;
    private Key key;
    private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
    private final RedisTemplate<String, String> redisTemplate;

    @PostConstruct
    public void init() {
        byte[] bytes = Base64.getDecoder().decode(secretKey);
        key = Keys.hmacShaKeyFor(bytes);
    }

    // Access Token 생성
    public String createAccessToken(Long userId, String username, String userRole) {
        Date date = new Date();

        return Jwts.builder()
            .setSubject(String.valueOf(userId))
            .claim("username", username)
            .claim("userRole", "ROLE_" + userRole)
            .claim("tokenType", "ACCESS")
            .setExpiration(new Date(date.getTime() + TOKEN_TIME))
            .setIssuedAt(date)
            .signWith(key, signatureAlgorithm)
            .compact();
    }

    // Refresh Token 생성 및 Redis 저장
    public String createRefreshToken(Long userId) {
        Date date = new Date();

        String refreshToken = Jwts.builder()
            .setSubject(String.valueOf(userId))
            .claim("tokenType", "REFRESH")
            .setExpiration(new Date(date.getTime() + REFRESH_TOKEN_TIME))
            .setIssuedAt(date)
            .signWith(key, signatureAlgorithm)
            .compact();

        saveRefreshTokenToRedis(userId, refreshToken);
        return refreshToken;
    }

    // Redis에 Refresh Token 저장
    private void saveRefreshTokenToRedis(Long userId, String refreshToken) {
        String key = "refresh_token:" + userId;
        redisTemplate.opsForValue().set(key, refreshToken, REFRESH_TOKEN_EXPIRE_DAYS, TimeUnit.DAYS);
    }

    // Redis에서 Refresh Token 조회
    private String getRefreshTokenFromRedis(Long userId) {
        String key = "refresh_token:" + userId;
        return redisTemplate.opsForValue().get(key);
    }

    // Redis에서 Refresh Token 삭제
    public void deleteRefreshTokenFromRedis(Long userId) {
        String key = "refresh_token:" + userId;
        redisTemplate.delete(key);
    }

    // Refresh Token 검증 (JWT + Redis)
    public boolean validateRefreshToken(String refreshToken) {
        try {
            if (!validateToken(refreshToken)) {
                return false;
            }

            if (!"REFRESH".equals(getTokenType(refreshToken))) {
                return false;
            }

            Long userId = getUserIdFromToken(refreshToken);
            String storedToken = getRefreshTokenFromRedis(userId);
            return storedToken != null && storedToken.equals(refreshToken);

        } catch (Exception e) {
            log.error("Refresh Token 검증 실패: {}", e.getMessage());
            return false;
        }
    }

    // 토큰 검증
    public boolean validateToken(String token) {
        try {
            extractClaims(token);
            return true;
        } catch (Exception e) {
            log.error("토큰 검증 실패: {}", e.getMessage());
            return false;
        }
    }

    // 토큰 타입 확인
    public String getTokenType(String token) {
        return extractClaims(token).get("tokenType", String.class);
    }

    public String substringToken(String tokenValue) {
        if (StringUtils.hasText(tokenValue) && tokenValue.startsWith(BEARER_PREFIX)) {
            return tokenValue.substring(7);
        }
        throw new IllegalArgumentException("유효하지 않은 JWT 형식입니다.");
    }

    public Claims extractClaims(String token) {
        return Jwts.parserBuilder()
            .setSigningKey(key)
            .build()
            .parseClaimsJws(token)
            .getBody();
    }

    public Long getUserIdFromToken(String token) {
        return Long.parseLong(extractClaims(token).getSubject());
    }
}
