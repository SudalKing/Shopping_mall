package com.example.shoppingmall.domain.awsS3.repository;

import com.example.shoppingmall.domain.awsS3.entity.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductImageRepository extends JpaRepository<ProductImage, Long> {
    List<ProductImage> findAllByProductId(Long productId);
    void deleteAllByProductId(Long productId);

    @Query(value = "select upload_file_url from product_image where product_id = :productId", nativeQuery = true)
    List<String> findUploadFileUrlsByProductId(Long productId);
}
