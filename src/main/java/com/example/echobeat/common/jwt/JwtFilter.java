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
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final CustomUserDetailsService userDetailsService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
        FilterChain filterChain) throws ServletException, IOException {

        String url = request.getRequestURI();

        if (url.equals("/signup") || url.equals("/login") ||
            url.startsWith("/swagger-ui") || url.startsWith("/v3/api-docs") ||
            url.equals("/swagger-ui.html") || url.startsWith("/h2-console")) {

            filterChain.doFilter(request, response);
            return;
        }

        String bearerToken = request.getHeader("Authorization");

        if (bearerToken == null) {
            sendErrorResponse(response, ExceptionCode.MISSING_TOKEN);
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
            sendErrorResponse(response, ExceptionCode.INVALID_JWT_SIGNATURE);
        } catch (ExpiredJwtException e) {
            log.error("JWT 만료", e);
            sendErrorResponse(response, ExceptionCode.EXPIRED_TOKEN);
        } catch (UnsupportedJwtException e) {
            log.error("지원되지 않는 JWT", e);
            sendErrorResponse(response, ExceptionCode.UNSUPPORTED_TOKEN);
        } catch (IllegalArgumentException e) {
            log.error("잘못된 JWT 토큰", e);
            sendErrorResponse(response, ExceptionCode.INVALID_TOKEN);
        } catch (io.jsonwebtoken.security.SignatureException e) {
            log.error("JWT 서명 검증 실패", e);
            sendErrorResponse(response, ExceptionCode.INVALID_JWT_SIGNATURE);
        } catch (Exception e) {
            log.error("JWT 처리 중 예외 발생", e);
            sendErrorResponse(response, ExceptionCode.INVALID_JWT_SIGNATURE);
        }
    }

    private void sendErrorResponse(HttpServletResponse response, ExceptionCode exceptionCode) throws IOException {
        response.setStatus(exceptionCode.getHttpStatus().value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");

        Map<String, Object> errorResponse = new HashMap<>();
        Map<String, String> error = new HashMap<>();
        error.put("code", exceptionCode.name());
        error.put("message", exceptionCode.getMessage());
        errorResponse.put("error", error);

        response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
    }
}
