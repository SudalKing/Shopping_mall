package com.example.shoppingmall.domain.post.service;

import com.example.shoppingmall.domain.post.dto.PostDto;
import com.example.shoppingmall.domain.post.entity.Post;
import com.example.shoppingmall.domain.post.repository.PostLikeRepository;
import com.example.shoppingmall.domain.post.repository.PostRepository;
import com.example.shoppingmall.util.CursorRequest;
import com.example.shoppingmall.util.PageCursor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class PostReadService {
    private final PostRepository postRepository;
    private final PostLikeRepository postLikeRepository;

    public PostDto getPost(Long postId){
        return postRepository.findById(postId)
                .map(this::toDto)
                .orElseThrow();
    }

    public List<PostDto> getPostsByUserId(Long userId){
        return postRepository.findAllByUserId(userId)
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public PageCursor<Post> getPostsByCursor(CursorRequest cursorRequest){
        var posts = findAllBy(cursorRequest);
        var nextKey = getNextKey(posts);

        return new PageCursor<>(cursorRequest.next(nextKey), posts);
    }

    private List<Post> findAllBy(CursorRequest cursorRequest) {
        if(cursorRequest.hasKey()){
            return postRepository.findAllProductsByCursorHasKey(cursorRequest.getKey(), cursorRequest.getSize());
        } else{
            return postRepository.findAllProductsByCursorNoKey(cursorRequest.getSize());
        }
    }

    public PostDto toDto(Post post){
        return new PostDto(
                post.getId(),
                post.getTitle(),
                post.getContents(),
                post.getCreatedAt(),
                postLikeRepository.countAllByPostId(post.getId())
        );
    }

    private Long getNextKey(List<Post> posts){
        return posts.stream()
                .mapToLong(Post::getId)
                .min()
                .orElse(CursorRequest.NONE_KEY);
    }
}
