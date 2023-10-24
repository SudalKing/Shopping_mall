package com.example.shoppingmall.domain.user.repository;

import com.example.shoppingmall.domain.user.entity.UserBirth;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BirthRepository extends JpaRepository<UserBirth, Long> {
    UserBirth findByUserId(Long userId);
    void deleteByUserId(Long userId);
}
