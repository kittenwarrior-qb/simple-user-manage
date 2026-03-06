package com.example.management_demo.repository.database.users;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findByUserEmail(String userEmail);

    Optional<UserEntity> findByUserName(String userName);

    boolean existsByUserEmail(String userEmail);

    boolean existsByUserName(String userName);

    @Query("SELECT u FROM UserEntity u WHERE " +
           "(:userName IS NULL OR :userName = '' OR u.userName LIKE %:userName%) AND " +
           "(:userEmail IS NULL OR :userEmail = '' OR u.userEmail LIKE %:userEmail%) AND " +
           "(:phoneNumber IS NULL OR :phoneNumber = '' OR u.phoneNumber LIKE %:phoneNumber%) AND " +
           "(:status IS NULL OR :status = '' OR u.status = :status)")
    Page<UserEntity> filterUsers(
            @Param("userName") String userName,
            @Param("userEmail") String userEmail,
            @Param("phoneNumber") String phoneNumber,
            @Param("status") String status,
            Pageable pageable
    );
}
