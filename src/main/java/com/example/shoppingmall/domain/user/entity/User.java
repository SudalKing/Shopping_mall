package com.example.shoppingmall.domain.user.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;
import java.util.Objects;

@NoArgsConstructor
@Getter
@Builder
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String email;
    private String password;
    private LocalDateTime createdAt;

    public User(Long id, String email, String password, LocalDateTime createdAt) {
        this.id = id;
        this.email = Objects.requireNonNull(email);
        this.password = Objects.requireNonNull(password);
        this.createdAt = createdAt == null ? LocalDateTime.now() : createdAt;
    }

    public void changeEmail(String newEmail){
        Objects.requireNonNull(newEmail);
        email = newEmail;
    }

    public void changePassword(String newPassword){
        Objects.requireNonNull(newPassword);
        password = newPassword;
    }
}
