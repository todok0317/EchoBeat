package com.example.echobeat.common.jwt;

import com.example.echobeat.common.security.CustomUserDetailsService;
import com.example.echobeat.common.security.CustomUserPrincipal;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final CustomUserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
        FilterChain filterChain) throws ServletException, IOException {

        String url = request.getRequestURI();

        // 인증이 필요 없는 URL
        if (url.equals("/signup") || url.equals("/login") ||
            url.startsWith("/swagger-ui") || url.startsWith("/v3/api-docs") ||
            url.equals("/swagger-ui.html") || url.startsWith("/h2-console")) {

            filterChain.doFilter(request, response);
            return;
        }

        String bearerToken = request.getHeader("Authorization");

        if (bearerToken == null) {
            response.sendError(HttpStatus.UNAUTHORIZED.value(), "JWT 토큰이 필요합니다.");
            return;
        }

        try {
            String token = jwtUtil.substringToken(bearerToken);
            Claims claims = jwtUtil.extractClaims(token);

            Long userId = Long.parseLong(claims.getSubject());

            CustomUserPrincipal userDetails = userDetailsService.loadUserById(userId);
            UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

            SecurityContextHolder.getContext().setAuthentication(authentication);
            filterChain.doFilter(request, response);

        } catch (SecurityException | MalformedJwtException e) {
            log.error("잘못된 JWT 서명", e);
            response.sendError(HttpStatus.UNAUTHORIZED.value(), "잘못된 JWT 서명입니다.");
        } catch (ExpiredJwtException e) {
            log.error("JWT 만료", e);
            response.sendError(HttpStatus.UNAUTHORIZED.value(), "JWT 토큰이 만료되었습니다.");
        } catch (UnsupportedJwtException e) {
            log.error("지원되지 않는 JWT", e);
            response.sendError(HttpStatus.BAD_REQUEST.value(), "지원되지 않는 JWT 토큰입니다.");
        } catch (IllegalArgumentException e) {
            log.error("잘못된 JWT 토큰", e);
            response.sendError(HttpStatus.BAD_REQUEST.value(), "잘못된 JWT 토큰입니다.");
        } catch (io.jsonwebtoken.security.SignatureException e) {
            log.error("JWT 서명 검증 실패", e);
            response.sendError(HttpStatus.UNAUTHORIZED.value(), "JWT 서명 검증에 실패했습니다.");
        } catch (Exception e) {
            log.error("JWT 처리 중 예외 발생", e);
            response.sendError(HttpStatus.INTERNAL_SERVER_ERROR.value(), "JWT 처리 중 오류가 발생했습니다.");
        }
    }
}
