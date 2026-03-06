package com.example.management_demo.service.users;

import com.example.management_demo.dto.users.User;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class UserUseCaseService {

    private final UserCommandService userCommandService;
    private final UserQueryService userQueryService;

    // ── Command ─────────────────────────────────────────────────
    @Transactional
    public User createUser(User user) {
        return userCommandService.createUser(user);
    }

    @Transactional
    public List<User> createRandomUsers(int quantity) {
        return userCommandService.createRandomUsers(quantity);
    }

    @Transactional
    public User updateUser(Long id, User user) {
        return userCommandService.updateUser(id, user);
    }

    @Transactional
    public void deleteUser(Long id) {
        userCommandService.deleteUser(id);
    }

    @Transactional
    public User restrictUser(Long id) {
        return userCommandService.restrictUser(id);
    }

    @Transactional
    public User activateUser(Long id) {
        return userCommandService.activateUser(id);
    }

    // ── Query ────────────────────────────────────────────────────
    public User getUserById(Long id) {
        return userQueryService.getUserById(id);
    }

    public List<User> getAllUsers() {
        return userQueryService.getAllUsers();
    }

    public Page<User> getUsersWithFilter(String userName, String userEmail, 
                                         String phoneNumber, String status, 
                                         Pageable pageable) {
        return userQueryService.getUsersWithFilter(userName, userEmail, phoneNumber, status, pageable);
    }

    @Transactional
    public User assignTeam(Long userId, Long teamId) {
        return userCommandService.assignTeam(userId, teamId);
    }
}
