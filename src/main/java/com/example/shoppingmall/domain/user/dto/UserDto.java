package com.example.shoppingmall.domain.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Builder
@Schema(description = "회원 Response class")
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
