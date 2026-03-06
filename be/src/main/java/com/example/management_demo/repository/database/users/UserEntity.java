package com.example.management_demo.repository.database.users;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, columnDefinition = "VARCHAR(255)")
    private String userName;

    @Column(nullable = false, unique = true, columnDefinition = "VARCHAR(255)")
    private String userEmail;

    @Column(nullable = false, columnDefinition = "VARCHAR(255)")
    private String password;

    @Column(columnDefinition = "TEXT")
    private String userAddress;

    @Column(columnDefinition = "VARCHAR(20)")
    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private Status status = Status.RESTRICTED;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private Role role = Role.USER;

    public enum Status {
        ACTIVE,
        RESTRICTED
    }

    public enum Role {
        USER,
        ADMIN
    }
}
