package com.example.shoppingmall.domain.post.repository;

import com.example.shoppingmall.domain.post.entity.PostLike;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostLikeRepository extends JpaRepository<PostLike, Long> {
    Long countAllByPostId(Long postId);
    PostLike findByUserIdAndPostId(Long userId, Long postId);
    void deleteByUserIdAndPostId(Long userId, Long postId);
}
