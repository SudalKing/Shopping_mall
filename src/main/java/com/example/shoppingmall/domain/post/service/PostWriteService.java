package com.example.shoppingmall.domain.post.service;

import com.example.shoppingmall.domain.post.dto.PostCommand;
import com.example.shoppingmall.domain.post.entity.Post;
import com.example.shoppingmall.domain.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
public class PostWriteService {
    private final PostRepository postRepository;

    public Post createPost(PostCommand postCommand){
        var post = Post.builder()
                .userId(postCommand.getUserId())
                .title(postCommand.getTitle())
                .contents(postCommand.getContents())
                .createdAt(LocalDateTime.now())
                .build();
        return postRepository.save(post);
    }
}
