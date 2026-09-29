package com.narvasoft.apirest.models;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "user_totp_secrets")
@Getter @Setter @ToString @EqualsAndHashCode
public class UserTotpSecret {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Column(name = "secret_key", nullable = false, length = 64)
    private String secretKey;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private Boolean enabled = false;
}