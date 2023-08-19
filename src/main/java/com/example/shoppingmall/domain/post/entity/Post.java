package com.example.shoppingmall.domain.post.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Objects;

@NoArgsConstructor
@Getter
@Entity
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private Long userId;

    @NotBlank
    private String title;

    @NotBlank
    private String contents;

    @NotNull
    private LocalDateTime createdAt;

    @Builder
    public Post(Long id, Long userId, String title, String contents, LocalDateTime createdAt) {
        this.id = id;
        this.userId = Objects.requireNonNull(userId);
        this.title = Objects.requireNonNull(title);
        this.contents = Objects.requireNonNull(contents);
        this.createdAt = createdAt == null ? LocalDateTime.now() : createdAt;
    }
}
