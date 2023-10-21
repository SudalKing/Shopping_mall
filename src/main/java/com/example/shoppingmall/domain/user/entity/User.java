package com.example.shoppingmall.domain.user.entity;

import com.example.shoppingmall.domain.user.util.Role;
import com.example.shoppingmall.domain.user.util.SocialType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.time.LocalDateTime;
import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@Entity
//@Table(uniqueConstraints = {
//        @UniqueConstraint(name = "NICKNAME_EMAIL_UNIQUE", columnNames = {"nickname", "email"})
//})
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String nickname;

    @Pattern(regexp = "^\\d{2,3}-\\d{3,4}-\\d{4}$",message = "핸드폰 번호의 양식과 맞지 않습니다. 01x-xxx(x)-xxxx")
    private String phoneNumber;

    @Email
    private String email;

    @Column(nullable = false)
    private String password;

    @NotNull
    private LocalDateTime createdAt;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Enumerated(EnumType.STRING)
    private SocialType socialType;

    private String socialId;

    private String refreshToken;

    private boolean enabled;

    @PrePersist
    public void setCreatedAt(){
        createdAt = LocalDateTime.now();
    }

    public void changeNickname(String newNickname){
        Objects.requireNonNull(newNickname);
        nickname = newNickname;
    }

    public void updateUserRole() {
        this.role = Role.USER;
    }
    public void updateRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
