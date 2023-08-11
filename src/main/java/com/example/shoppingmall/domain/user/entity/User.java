package com.example.shoppingmall.domain.user.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Objects;

@NoArgsConstructor
@Getter
@Entity
@Table(uniqueConstraints = {
        @UniqueConstraint(name = "NICKNAME_EMAIL_UNIQUE", columnNames = {"nickname", "email"})
})
public class User{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String nickname;

    @NotNull
    private String email;

    @Column(nullable = false)
    private String password;

    @NotNull
    private LocalDateTime createdAt;

    @Builder
    public User(Long id, String nickname, String email, String password, LocalDateTime createdAt) {
        this.id = id;
        this.nickname = Objects.requireNonNull(nickname);
        this.email = Objects.requireNonNull(email);
        this.password = Objects.requireNonNull(password);
        this.createdAt = createdAt == null ? LocalDateTime.now() : createdAt;
    }

    @PrePersist
    public void setCreatedAt(){
        createdAt = LocalDateTime.now();
    }

    public void changeNickname(String newNickname){
        Objects.requireNonNull(newNickname);
        nickname = newNickname;
    }
}
