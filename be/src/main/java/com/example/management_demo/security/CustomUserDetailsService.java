package com.example.management_demo.security;

import com.example.management_demo.repository.database.users.UserEntity;
import com.example.management_demo.repository.database.users.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findByUserEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

        return org.springframework.security.core.userdetails.User.builder()
                .username(userEntity.getUserEmail())
                .password(userEntity.getPassword())
                .roles(userEntity.getRole().name())
                .disabled(userEntity.getStatus() != UserEntity.Status.ACTIVE)
                .build();
    }
}
