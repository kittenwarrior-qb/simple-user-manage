package com.example.management_demo.service.auth;

import com.example.management_demo.dto.users.User;
import com.example.management_demo.repository.database.users.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AuthUseCaseService {

    private final AuthCommandService authCommandService;
    private final UserRepository userRepository;

    // ── Command ─────────────────────────────────────────────────
    @Transactional
    public String login(String email, String password) {
        return authCommandService.login(email, password);
    }

    @Transactional
    public String register(String userName, String email, String password,
                          String userAddress, String phoneNumber) {
        return authCommandService.register(userName, email, password, userAddress, phoneNumber);
    }

    // ── Query ────────────────────────────────────────────────────
    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        
        var userEntity = userRepository.findByUserEmail(email)
            .orElseThrow(() -> new IllegalArgumentException("User not found"));
        
        return new User(
            userEntity.getId(),
            userEntity.getUserName(),
            userEntity.getUserEmail(),
            null, // Don't expose password
            userEntity.getUserAddress(),
            userEntity.getPhoneNumber(),
            userEntity.getStatus().name(),
            userEntity.getRole().name()
        );
    }

    public void logout() {
        SecurityContextHolder.clearContext();
    }
}
