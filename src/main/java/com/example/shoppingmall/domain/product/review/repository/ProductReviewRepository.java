package com.example.shoppingmall.domain.product.review.repository;

import com.example.shoppingmall.domain.product.review.entity.ProductReview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductReviewRepository extends JpaRepository<ProductReview, Long> {
    ProductReview findProductReviewById(Long id);
    List<ProductReview> findAllByUserIdOrderByCreatedAt(Long userId);
    List<ProductReview> findAllByProductId(Long productId);

    Integer countAllByProductId(Long productId);

    @Query(value = "select sum(rating) from product_review " +
            "where product_id = :productId", nativeQuery = true)
    Integer sumTotalReviewByProductId(@Param("productId") Long productId);

    Integer countByProductIdAndRating(Long productId, Integer rating);


    @Query(value = "select * from product_review " +
            "where id < :id and product_id = :productId " +
            "order by id desc limit :size", nativeQuery = true)
    List<ProductReview> findAllByProductIdByCursorOrderByIdDescHasKey(@Param("id") Long id,
                                                                      @Param("size") int size,
                                                                      @Param("productId") Long productId);

    @Query(value = "select * from product_review " +
            "where product_id = :productId " +
            "order by id desc limit :size", nativeQuery = true)
    List<ProductReview> findAllByProductIdByCursorOrderByIdDescNoKey(@Param("size") int size,
                                                                     @Param("productId") Long productId);



    @Query(value = "select * from product_review as pr " +
            "left join review_like_score as rls on pr.id = rls.review_id " +
            "where rls.review_score < :likeScore and pr.product_id = :productId " +
            "order by rls.review_score desc limit :size", nativeQuery = true)
    List<ProductReview> findAllByProductIdByCursorOrderByLikeDescHasKey(@Param("likeScore") Double likeScore,
                                                                        @Param("size") int size,
                                                                        @Param("productId") Long productId);

    @Query(value = "select * from product_review as pr " +
            "left join review_like_score as rls on pr.id = rls.review_id " +
            "where pr.product_id = :productId " +
            "order by rls.review_score desc limit :size", nativeQuery = true)
    List<ProductReview> findAllByProductIdByCursorOrderByLikeDescNoKey(@Param("size") int size,
                                                                       @Param("productId") Long productId);




    @Query(value = "select pr.* from product_review as pr " +
            "inner join product_review_image as pri on pr.id = pri.review_id " +
            "where pr.id < :id and pr.product_id = :productId " +
            "order by id desc limit :size", nativeQuery = true)
    List<ProductReview> findAllExistImageByProductIdByCursorOrderByIdDescHasKey(@Param("id") Long id,
                                                                                @Param("size") int size,
                                                                                @Param("productId") Long productId);

    @Query(value = "select pr.* from product_review as pr " +
            "inner join product_review_image as pri on pr.id = pri.review_id " +
            "where pr.product_id = :productId " +
            "order by id desc limit :size", nativeQuery = true)
    List<ProductReview> findAllExistImageByProductIdByCursorOrderByIdDescNoKey(@Param("size") int size,
                                                                               @Param("productId") Long productId);

}
