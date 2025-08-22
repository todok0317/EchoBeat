package com.echobeat.user.config;

import com.echobeat.common.jwt.JwtFilter;
import com.echobeat.common.jwt.JwtUtil;
import com.echobeat.common.security.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtUtil jwtUtil;
    private final CustomUserDetailsService userDetailsService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.csrf(AbstractHttpConfigurer::disable)
            .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> {
                // User Service 전용 경로 설정
                auth.requestMatchers("/auth/signup", "/auth/login").permitAll();
                auth.requestMatchers("/auth/refresh").permitAll();
                auth.requestMatchers("/actuator/health", "/actuator/info").permitAll();
                auth.requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll();
                auth.requestMatchers("/admin/**").hasRole("ADMIN");
                auth.anyRequest().authenticated();
            })
            .headers(headers -> headers.frameOptions().disable())
            .addFilterBefore(jwtFilter(), UsernamePasswordAuthenticationFilter.class);

        return httpSecurity.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration)
        throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public JwtFilter jwtFilter() {
        return new JwtFilter(jwtUtil, userDetailsService);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
