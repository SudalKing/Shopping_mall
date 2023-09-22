package com.example.shoppingmall.domain.user.entity;

import com.example.shoppingmall.domain.cart.entity.Cart;
import com.example.shoppingmall.domain.user.auth.CustomAuthority;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

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

    private boolean enabled;

    @JsonIgnore
    @OneToOne(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @ToString.Exclude
    private Cart cart;

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @ToString.Exclude
    private Set<CustomAuthority> authorities = new HashSet<>();

    @PrePersist
    public void setCreatedAt(){
        createdAt = LocalDateTime.now();
    }

    public void changeNickname(String newNickname){
        Objects.requireNonNull(newNickname);
        nickname = newNickname;
    }

    public void setAuthorities(Set<CustomAuthority> authorities){
        this.authorities = authorities;
    }
}
