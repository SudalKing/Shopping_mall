package com.example.shoppingmall.domain.user.auth;

import com.example.shoppingmall.domain.user.entity.User;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
@Entity
public class CustomAuthority implements GrantedAuthority {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String authority;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    @ToString.Exclude
    private User user;

    public CustomAuthority(String authority, User user) {
        this.authority = authority;
        this.user = user;
    }
}
