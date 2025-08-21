package com.example.echobeat.domain.user.repository;

import com.example.echobeat.domain.user.entity.User;
import com.example.echobeat.domain.user.enums.UserRole;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // username으로 사용자 찾기
    Optional<User> findByUsername(String username);
    
    // email로 사용자 찾기
    Optional<User> findByEmail(String email);
    
    // username 존재 여부 확인
    boolean existsByUsername(String username);
    
    // email 존재 여부 확인
    boolean existsByEmail(String email);
    
    // refreshToken으로 사용자 찾기
    Optional<User> findByRefreshToken(String refreshToken);
    
    // role로 사용자 목록 조회
    List<User> findByRole(UserRole role);
    
    // username과 email로 사용자 찾기 (비밀번호 찾기 등에 사용)
    Optional<User> findByUsernameAndEmail(String username, String email);
}
