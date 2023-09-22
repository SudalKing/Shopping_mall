package com.example.shoppingmall.domain.post.service;

import com.example.shoppingmall.domain.post.entity.PostLike;
import com.example.shoppingmall.domain.post.repository.PostLikeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
public class PostLikeWriteService {
    private final PostLikeRepository postLikeRepository;

    public void createPostLike(Long userId, Long postId){
        var findPostLike = postLikeRepository.findByUserIdAndPostId(userId, postId);
        if (findPostLike == null) {
            var postLike = PostLike.builder()
                    .userId(userId)
                    .postId(postId)
                    .createdAt(LocalDateTime.now())
                    .build();
            postLikeRepository.save(postLike);
        } else {
            cancelPostLike(userId, postId);
        }
    }

    public void cancelPostLike(Long userId, Long postId){
        postLikeRepository.deleteByUserIdAndPostId(userId, postId);
    }
}
