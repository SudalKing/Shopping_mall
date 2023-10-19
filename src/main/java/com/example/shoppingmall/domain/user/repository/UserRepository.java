package com.example.shoppingmall.domain.user.repository;

import com.example.shoppingmall.domain.user.entity.User;
import com.example.shoppingmall.domain.user.util.SocialType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
     Optional<User> findByEmail(String email);
     User findUserByEmail(String email);
     User findUserById(Long id);
     User findBySocialTypeAndSocialId(SocialType socialType, String socialId);
     Optional<User> findByRefreshToken(String refreshToken);
}
