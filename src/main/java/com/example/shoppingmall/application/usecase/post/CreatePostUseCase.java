package com.example.shoppingmall.application.usecase.post;

import com.example.shoppingmall.domain.awsS3.service.PostImageWriteService;
import com.example.shoppingmall.domain.post.dto.PostCommand;
import com.example.shoppingmall.domain.post.dto.PostDto;
import com.example.shoppingmall.domain.post.service.PostReadService;
import com.example.shoppingmall.domain.post.service.PostWriteService;
import com.example.shoppingmall.util.AmazonS3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class CreatePostUseCase {
    private final PostWriteService postWriteService;
    private final PostReadService postReadService;
    private final PostImageWriteService postImageWriteService;
    private final AmazonS3Service amazonS3Service;

    public PostDto execute(PostCommand postCommand, String fileType, List<MultipartFile> multipartFiles){
        var post = postWriteService.createPost(postCommand);
        var s3FileDtoList = amazonS3Service.uploadFiles(fileType, multipartFiles);

        postImageWriteService.createPostImage(post.getId(), s3FileDtoList);

        return postReadService.getPost(post.getId());
    }
}
