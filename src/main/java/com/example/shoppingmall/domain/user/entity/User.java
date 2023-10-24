package com.example.shoppingmall.domain.user.entity;

import com.example.shoppingmall.domain.user.util.Role;
import com.example.shoppingmall.domain.user.util.SocialType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@Entity
@Table(uniqueConstraints = {
        @UniqueConstraint(name = "EMAIL_UNIQUE", columnNames = {"email"})
})
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Pattern(regexp = "^\\d{2,3}-\\d{3,4}-\\d{4}$",message = "핸드폰 번호의 양식과 맞지 않습니다. 01x-xxx(x)-xxxx")
    @Column(nullable = false)
    private String phoneNumber;

    @Email
    @Column(nullable = false, unique = true)
    private String email;

    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[~!@#$%^&*])[A-Za-z\\d~!@#$%^&*]{8,16}$",
    message = "비밀번호는 8 ~ 16자, 영문, 숫자, 특수문자를 하나 이상 포함하여야 합니다.")
    @Column(nullable = false)
    private String password;

    private LocalDateTime createdAt;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Enumerated(EnumType.STRING)
    private SocialType socialType;

    private String socialId;

    private String refreshToken;

    private boolean enabled;

    private boolean infoSet;

    @PrePersist
    public void setCreatedAt(){
        createdAt = LocalDateTime.now();
    }

    public void updateUserRole() {
        this.role = Role.USER;
    }
    public void updateRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
    public void updatePassword(String password) {
        this.password = password;
    }
    public void updatePhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    public void updateInfoSet(){
        this.infoSet = true;
    }
}
