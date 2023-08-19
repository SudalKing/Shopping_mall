package com.example.shoppingmall.domain.user.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class UserDto {
    private Long id;
    private String nickname;
    private String phoneNumber;
    private String email;
    private String password;
    private LocalDateTime createdAt;
    private boolean enabled;

    public UserDto(Long id, String nickname, String phoneNumber, String email, String password, LocalDateTime createdAt, boolean enabled) {
        this.id = id;
        this.nickname = nickname;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.password = password;
        this.createdAt = createdAt;
        this.enabled = enabled;
    }
}
