package com.echobeat.user.security;

import com.echobeat.common.security.CustomUserDetailsService;
import com.echobeat.common.security.CustomUserPrincipal;
import com.echobeat.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements CustomUserDetailsService {
    
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
            .map(user -> CustomUserPrincipal.create(
                user.getId(),
                user.getUsername(),
                user.getPassword(),
                user.getRoleNames()
            ))
            .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
    }

    @Override
    public CustomUserPrincipal loadUserById(Long userId) {
        return userRepository.findById(userId)
            .map(user -> CustomUserPrincipal.create(
                user.getId(),
                user.getUsername(),
                user.getPassword(),
                user.getRoleNames()
            ))
            .orElseThrow(() -> new UsernameNotFoundException("User not found with id: " + userId));
    }
}
