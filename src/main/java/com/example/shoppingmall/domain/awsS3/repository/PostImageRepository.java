package com.example.shoppingmall.domain.awsS3.repository;

import com.example.shoppingmall.domain.awsS3.entity.PostImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostImageRepository extends JpaRepository<PostImage, Long> {
    List<PostImage> findAllByPostId(Long postId);
}
