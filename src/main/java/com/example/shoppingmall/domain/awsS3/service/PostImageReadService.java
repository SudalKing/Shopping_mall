package com.example.shoppingmall.domain.awsS3.service;

import com.example.shoppingmall.domain.awsS3.entity.PostImage;
import com.example.shoppingmall.domain.awsS3.repository.PostImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class PostImageReadService {
    private final PostImageRepository postImageRepository;

    public List<PostImage> readImages(Long postId){
        return postImageRepository.findAllByPostId(postId);
    }
}
