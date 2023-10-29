package com.example.shoppingmall.domain.awsS3.repository;

import com.example.shoppingmall.domain.awsS3.entity.ProductReviewImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductReviewImageRepository extends JpaRepository<ProductReviewImage, Long> {

    @Query(value = "select upload_file_url from product_review_image where review_id = :reviewId", nativeQuery = true)
    List<String> findUploadFileUrlsByReviewId(Long reviewId);
    List<ProductReviewImage> findAllByReviewId(Long reviewId);
    void deleteAllByReviewId(Long reviewId);
}
