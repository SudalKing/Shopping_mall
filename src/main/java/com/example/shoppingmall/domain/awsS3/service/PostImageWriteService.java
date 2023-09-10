package com.example.shoppingmall.domain.awsS3.service;

import com.example.shoppingmall.domain.awsS3.dto.S3FileDto;
import com.example.shoppingmall.domain.awsS3.entity.PostImage;
import com.example.shoppingmall.domain.awsS3.repository.PostImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class PostImageWriteService {
    private final PostImageRepository postImageRepository;

    @Transactional
    public void createPostImage(Long postId, List<S3FileDto> s3FileDtoList){
        if (s3FileDtoList == null) {
            var postImage = PostImage.builder()
                    .postId(postId)
                    .originalFileName(null)
                    .uploadFileName(null)
                    .uploadFilePath(null)
                    .uploadFileUrl(null)
                    .deleted(false)
                    .build();
        } else {
            for (var s3fileDto : s3FileDtoList) {
                var postImage = PostImage.builder()
                        .postId(postId)
                        .originalFileName(s3fileDto.getOriginalFileName())
                        .uploadFileName(s3fileDto.getUploadFileName())
                        .uploadFilePath(s3fileDto.getUploadFilePath())
                        .uploadFileUrl(s3fileDto.getUploadFileUrl())
                        .deleted(false)
                        .build();
                postImageRepository.save(postImage);
            }
        }
    }

}
