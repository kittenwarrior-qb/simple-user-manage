package com.example.management_demo.service.users;

import com.example.management_demo.dto.users.User;
import com.example.management_demo.repository.database.team.TeamRepository;
import com.example.management_demo.repository.database.users.UserEntity;
import com.example.management_demo.repository.database.users.UserMapper;
import com.example.management_demo.repository.database.users.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class UserCommandService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;    
    private final PasswordEncoder passwordEncoder;
    private final TeamRepository teamRepository;

    public List<User> createRandomUsers(int quantity) {
        if (quantity <= 0 || quantity > 100) {
            throw new IllegalArgumentException("Quantity must be between 1 and 100");
        }
        
        log.info("Starting to create {} random users", quantity);
        List<User> createdUsers = new ArrayList<>();
        Random random = new Random();
        int successCount = 0;
        int failCount = 0;
        
        for (int i = 0; i < quantity; i++) {
            try {
                String uniqueId = System.currentTimeMillis() + "_" + random.nextInt(10000);
                
                User randomUser = new User(
                    null, // id 
                    generateRandomUsername(uniqueId),
                    generateRandomEmail(uniqueId),
                    passwordEncoder.encode("password"), // default password
                    generateRandomAddress(random),
                    generateRandomPhone(random),
                    null, // status
                    null, // role 
                    null  // team
                );
                
                log.debug("Creating user {}/{}: {}", i + 1, quantity, randomUser.userEmail());
                
                UserEntity entity = userMapper.toEntity(randomUser);
                entity.setStatus(UserEntity.Status.ACTIVE);
                entity.setRole(UserEntity.Role.USER);
                User savedUser = userMapper.toUser(userRepository.save(entity));
                createdUsers.add(savedUser);
                successCount++;
                
                Thread.sleep(1);
                
            } catch (Exception e) {
                failCount++;
                log.error("Failed to create user {}/{}: {}", i + 1, quantity, e.getMessage());
            }
        }
        
        log.info("Completed: {} users created successfully, {} failed", successCount, failCount);
        return createdUsers;
    }
    
    private String generateRandomUsername(String uniqueId) {
        String[] prefixes = {"user", "test", "demo", "member"};
        return prefixes[new Random().nextInt(prefixes.length)] + "_" + uniqueId;
    }
    
    private @NonNull String generateRandomEmail(String uniqueId) {
        String[] domains = {"gmail.com", "yahoo.com", "outlook.com", "example.com"};
        return "user_" + uniqueId + "@" + domains[new Random().nextInt(domains.length)];
    }
    
    private String generateRandomAddress(Random random) {
        String[] streets = {"Nguyen Hue", "Le Loi", "Tran Hung Dao", "Hai Ba Trung", "Ly Thuong Kiet"};
        String[] cities = {"Ho Chi Minh", "Ha Noi", "Da Nang", "Can Tho", "Nha Trang"};
        int number = random.nextInt(999) + 1;
        return number + " " + streets[random.nextInt(streets.length)] + ", " + cities[random.nextInt(cities.length)];
    }
    
    private String generateRandomPhone(Random random) {
        return "0" + (random.nextInt(9) + 1) + String.format("%08d", random.nextInt(100000000));
    }

    public User createUser(User user) {
        log.debug("Creating user with email: {}", user.userEmail());
        
        if (userRepository.existsByUserEmail(user.userEmail())) {
            log.warn("Failed to create user: Email already in use - {}", user.userEmail());
            throw new IllegalArgumentException("Email already in use: " + user.userEmail());
        }
        if (userRepository.existsByUserName(user.userName())) {
            log.warn("Failed to create user: Username already in use - {}", user.userName());
            throw new IllegalArgumentException("Username already in use: " + user.userName());
        }
        
        UserEntity entity = userMapper.toEntity(user);
        entity.setPassword(passwordEncoder.encode(user.password()));
        entity.setStatus(UserEntity.Status.ACTIVE);
        entity.setRole(UserEntity.Role.USER);
        User created = userMapper.toUser(userRepository.save(entity));
        log.info("User created successfully with email: {}", created.userEmail());
        return created;
    }

    public User updateUser(Long id, User user) {
        log.debug("Updating user with id: {}", id);
        
        UserEntity entity = userRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Failed to update: User not found with id: {}", id);
                    return new IllegalArgumentException("User not found with id: " + id);
                });
        
        userMapper.updateEntity(user, entity);
        User updated = userMapper.toUser(userRepository.save(entity));
        log.info("User updated successfully: {}", id);
        return updated;
    }

    public void deleteUser(Long id) {
        log.debug("Deleting user with id: {}", id);
        
        if (!userRepository.existsById(id)) {
            log.warn("Failed to delete: User not found with id: {}", id);
            throw new IllegalArgumentException("User not found with id: " + id);
        }
        
        userRepository.deleteById(id);
        log.info("User deleted successfully: {}", id);
    }

    public User restrictUser(Long id) {
        log.debug("Restricting user with id: {}", id);
        
        UserEntity entity = userRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Failed to restrict: User not found with id: {}", id);
                    return new IllegalArgumentException("User not found with id: " + id);
                });
        
        entity.setStatus(UserEntity.Status.RESTRICTED);
        User restricted = userMapper.toUser(userRepository.save(entity));
        log.info("User restricted successfully: {}", id);
        return restricted;
    }

    public User activateUser(Long id) {
        log.debug("Activating user with id: {}", id);
        
        UserEntity entity = userRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Failed to activate: User not found with id: {}", id);
                    return new IllegalArgumentException("User not found with id: " + id);
                });
        
        entity.setStatus(UserEntity.Status.ACTIVE);
        User activated = userMapper.toUser(userRepository.save(entity));
        log.info("User activated successfully: {}", id);
        return activated;
    }

    public User assignTeam(Long userId, Long teamId) {
        log.debug("Assigning user {} to team {}", userId, teamId);
        
        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.warn("Failed to assign team: User not found with id: {}", userId);
                    return new IllegalArgumentException("User not found with id: " + userId);
                });
        
        if (teamId == null) {
            userEntity.setTeam(null);
            log.info("User {} removed from team", userId);
        } else {
            var teamEntity = teamRepository.findById(teamId)
                    .orElseThrow(() -> {
                        log.warn("Failed to assign team: Team not found with id: {}", teamId);
                        return new IllegalArgumentException("Team not found with id: " + teamId);
                    });
            
            userEntity.setTeam(teamEntity);
            log.info("User {} assigned to team {}", userId, teamId);
        }
        
        User assigned = userMapper.toUser(userRepository.save(userEntity));
        return assigned;
    }
}
