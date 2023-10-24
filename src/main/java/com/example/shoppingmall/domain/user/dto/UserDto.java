package com.example.shoppingmall.domain.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@Schema(description = "회원 Response class")
public class UserDto {
    private Long id;
    private String name;
    private String phoneNumber;
    private String email;
    private String password;
    private LocalDateTime createdAt;
    private boolean enabled;
}
