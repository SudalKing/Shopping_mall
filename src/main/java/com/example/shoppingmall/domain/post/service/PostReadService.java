package com.example.shoppingmall.domain.post.service;

import com.example.shoppingmall.domain.awsS3.service.PostImageReadService;
import com.example.shoppingmall.domain.post.dto.PostDto;
import com.example.shoppingmall.domain.post.entity.Post;
import com.example.shoppingmall.domain.post.repository.PostLikeRepository;
import com.example.shoppingmall.domain.post.repository.PostRepository;
import com.example.shoppingmall.util.CursorRequest;
import com.example.shoppingmall.util.PageCursor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class PostReadService {
    private final PostRepository postRepository;
    private final PostLikeRepository postLikeRepository;
    private final PostImageReadService postImageReadService;

    public PostDto getPost(Long postId){
        return postRepository.findById(postId)
                .map(this::toDto)
                .orElseThrow();
    }

    public PageCursor<PostDto> getPostsByCursor(CursorRequest cursorRequest){
        var posts = toDtoList(findAllBy(cursorRequest));
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
        Long likeCount = postLikeRepository.countAllByPostId(post.getId());

        return new PostDto(
                post.getId(),
                post.getTitle(),
                post.getContents(),
                post.getCreatedAt(),
                likeCount,
                getUrls(post.getId())
        );
    }

    private List<String> getUrls(Long postId){
        var postImages = postImageReadService.readImages(postId);
        List<String> urls = new ArrayList<>();

        for (var postImage: postImages) {
            urls.add(postImage.getUploadFileUrl());
        }

        return urls;
    }

    public List<PostDto> toDtoList(List<Post> posts){
        return posts.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    private Long getNextKey(List<PostDto> posts){
        return posts.stream()
                .mapToLong(PostDto::getId)
                .min()
                .orElse(CursorRequest.NONE_KEY);
    }
}
