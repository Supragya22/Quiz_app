package com.practo.quiz.quiz_app.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf().disable() // Disables CSRF protection (for simplicity in this example)
            .authorizeRequests()
            .requestMatchers("/api/auth/login").permitAll() // Allow login endpoint without authentication
            .anyRequest().authenticated(); // All other endpoints require authentication
        
        return http.build();
    }
}
