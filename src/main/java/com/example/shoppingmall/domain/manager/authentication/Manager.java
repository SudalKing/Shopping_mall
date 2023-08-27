package com.example.shoppingmall.domain.manager.authentication;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class Manager {
    private Long id;
    private String username;
    private Set<GrantedAuthority> role;
}
