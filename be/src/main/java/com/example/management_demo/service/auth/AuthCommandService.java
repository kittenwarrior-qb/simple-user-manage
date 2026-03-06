package com.example.management_demo.service.auth;

import com.example.management_demo.repository.database.users.UserEntity;
import com.example.management_demo.repository.database.users.UserRepository;
import com.example.management_demo.security.CustomUserDetailsService;
import com.example.management_demo.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class AuthCommandService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final CustomUserDetailsService userDetailsService;
    private final JwtUtil jwtUtil;

    public String login(String email, String password) {
        log.debug("Attempting login for email: {}", email);
        
        try {
            authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, password)
            );
        } catch (BadCredentialsException e) {
            log.warn("Failed login attempt for email: {}", email);
            throw new IllegalArgumentException("Incorrect email or password");
        }

        final UserDetails userDetails = userDetailsService.loadUserByUsername(email);
        final String jwt = jwtUtil.generateToken(userDetails);
        
        log.info("User logged in successfully: {}", email);
        return jwt;
    }

    public String register(String userName, String email, String password, 
                          String userAddress, String phoneNumber) {
        log.debug("Attempting registration for email: {}", email);
        
        if (userRepository.existsByUserEmail(email)) {
            log.warn("Registration failed: Email already exists - {}", email);
            throw new IllegalArgumentException("Email already exists");
        }

        UserEntity userEntity = UserEntity.builder()
            .userName(userName)
            .userEmail(email)
            .password(passwordEncoder.encode(password))
            .userAddress(userAddress)
            .phoneNumber(phoneNumber)
            .status(UserEntity.Status.ACTIVE)
            .role(UserEntity.Role.USER)
            .build();

        userRepository.save(userEntity);
        
        final UserDetails userDetails = userDetailsService.loadUserByUsername(email);
        final String jwt = jwtUtil.generateToken(userDetails);
        
        log.info("User registered successfully: {}", email);
        return jwt;
    }
}
