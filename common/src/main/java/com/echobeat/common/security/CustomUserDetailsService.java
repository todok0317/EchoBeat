package com.echobeat.common.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * 각 서비스에서 구현해야 하는 UserDetailsService 인터페이스
 * 사용자 정보 조회를 위한 공통 인터페이스
 */
public interface CustomUserDetailsService extends UserDetailsService {
    
    /**
     * 사용자 ID로 사용자 정보 조회
     * @param userId 사용자 ID
     * @return CustomUserPrincipal 사용자 정보
     */
    CustomUserPrincipal loadUserById(Long userId);
}
