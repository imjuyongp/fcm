package com.fcm.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

/**
 * 연습용 보안 설정.
 * spring-boot-starter-security가 있으면 모든 요청이 기본 인증으로 막히므로,
 * FCM/Swagger 테스트를 위해 전부 허용한다. (실제 서비스에서는 인증 규칙으로 교체)
 */
@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth.anyRequest().permitAll());
        return http.build();
    }
}