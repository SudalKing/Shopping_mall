package com.example.shoppingmall.domain.follow.repository;

import com.example.shoppingmall.domain.follow.entity.Follow;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FollowRepository extends JpaRepository<Follow, Long> {
    List<Follow> findAllByFollowerId(Long followerId);
    List<Follow> findAllByFollowingId(Long followingId);
}
