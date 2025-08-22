package com.echobeat.common.security;

import java.security.Principal;
import java.util.Collection;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Getter
@RequiredArgsConstructor
public class CustomUserPrincipal implements UserDetails, Principal {

    private final Long id;
    private final String username;
    private final String password;
    private final Collection<? extends GrantedAuthority> authorities;

    // User 엔티티로부터 생성하는 정적 팩토리 메서드는 각 서비스에서 구현
    public static CustomUserPrincipal create(Long id, String username, String password, Collection<String> roles) {
        Collection<GrantedAuthority> authorities = roles.stream()
            .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
            .toList();
        
        return new CustomUserPrincipal(id, username, password, authorities);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    public Long getId() {
        return id;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public String getName() {
        return username;
    }
}
