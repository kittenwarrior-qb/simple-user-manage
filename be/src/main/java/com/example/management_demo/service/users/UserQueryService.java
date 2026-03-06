package com.example.management_demo.service.users;

import com.example.management_demo.dto.users.User;
import com.example.management_demo.repository.database.users.UserMapper;
import com.example.management_demo.repository.database.users.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserQueryService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    // @Cacheable(cacheNames = "jsonCacheTenMinutes", keyGenerator = "customKeyGenerator")
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .map(userMapper::toUser)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + id));
    }

    // @Cacheable(cacheNames = "jsonCacheFiveMinutes", keyGenerator = "customKeyGenerator")
    public List<User> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(userMapper::toUser)
                .toList();
    }

    // @Cacheable(cacheNames = "jsonCacheOneMinutes", keyGenerator = "customKeyGenerator")
    public Page<User> getUsersWithFilter(String userName, String userEmail,
                                         String phoneNumber, String status,
                                         Pageable pageable) {
        return userRepository.filterUsers(userName, userEmail, phoneNumber, status, pageable)
                .map(userMapper::toUser);
    }
}
