package com.example.shoppingmall.domain.user.repository;

import com.example.shoppingmall.domain.user.auth.CustomAuthority;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthRepository extends JpaRepository<CustomAuthority, Long> {
}
