package com.example.shoppingmall.domain.awsS3.repository;

import com.example.shoppingmall.domain.awsS3.entity.ProductReviewImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductReviewImageRepository extends JpaRepository<ProductReviewImage, Long> {

    @Query(value = "select upload_file_url from product_review_image where review_id = :reviewId", nativeQuery = true)
    List<String> findUploadFileUrlsByReviewId(Long reviewId);
    List<ProductReviewImage> findAllByReviewId(Long reviewId);
    void deleteByReviewId(Long reviewId);

    @Query(value = "select count(*) from product_review_image as pri " +
            "left join product_review as pr on pri.review_id = pr.id " +
            "where pr.product_id = :productId " +
            "group by pr.product_id", nativeQuery = true)
    Integer countPhotoAllByProductId(@Param("productId") Long productId);
}
