package com.example.echobeat.common.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) {
        return userRepository.findByUsername(username)
            .map(CustomUserPrincipal::new)
            .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    public CustomUserPrincipal loadUserById(Long userId) {
        return userRepository.findById(userId)
            .map(CustomUserPrincipal::new)
            .orElseThrow(() -> new UsernameNotFoundException("User not found with id: " + userId));
    }
}

