package com.example.shoppingmall.domain.product.repository;

import com.example.shoppingmall.domain.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Product findProductById(Long id);
    Product findTopByOrderByPriceDesc();

    //============================================= 전체 상품 조회 =====================================================
    // 0. 기본 최신순
    @Query(value = "select * from product where id < :id order by id desc limit :size", nativeQuery = true)
    List<Product> findAllProductsByCursorHasKey(@Param("id") Long id, @Param("size") int size);

    @Query(value = "select * from product order by id desc limit :size", nativeQuery = true)
    List<Product> findAllProductsByCursorNoKey(@Param("size") int size);

    // 1. 인기순
    //    상품 점수 필드 생성(하루마다 업데이트)
    @Query(value = "select * from product as p left join best as b on p.id = b.product_id " +
            "where p.id < :id order by b.score desc limit :size", nativeQuery = true)
    List<Product> findAllProductsByCursorHasKeyOrderByScore(@Param("id") Long id, @Param("size") int size);

    @Query(value = "select * from product as p left join best as b on p.id = b.product_id " +
            "order by b.score desc limit :size", nativeQuery = true)
    List<Product> findAllProductsByCursorNoKeyOrderByScore(@Param("size") int size);

    // 2. 낮은 가격순
    @Query(value = "select * from product where id < :id order by price asc limit :size", nativeQuery = true)
    List<Product> findAllProductsByCursorHasKeyOrderByPriceAsc(@Param("id") Long id, @Param("size") int size);

    @Query(value = "select * from product order by price asc limit :size", nativeQuery = true)
    List<Product> findAllProductsByCursorNoKeyOrderByPriceAsc(@Param("size") int size);

    // 3. 높은 가격순
    @Query(value = "select * from product where id < :id order by price desc limit :size", nativeQuery = true)
    List<Product> findAllProductsByCursorHasKeyOrderByPriceDesc(@Param("id") Long id, @Param("size") int size);

    @Query(value = "select * from product order by price desc limit :size", nativeQuery = true)
    List<Product> findAllProductsByCursorNoKeyOrderByPriceDesc(@Param("size") int size);

    // ==============================================================================================================

    // ====================================================== New ===================================================
    // 1. 기본(최신순)
    @Query(value = "select * from product where id < :id and created_at >= :days order by id desc limit :size",
            nativeQuery = true)
    List<Product> findAllProductsBetween7DaysByCursorHasKey(@Param("id") Long id, @Param("days")LocalDateTime days, @Param("size") int size);

    @Query(value = "select * from product where created_at >= :days order by id desc limit :size", nativeQuery = true)
    List<Product> findAllProductsBetween7DaysByCursorNoKey(@Param("days") LocalDateTime days, @Param("size") int size);

    // 2. 인기순
    @Query(value = "select * from product as p left join best as b on p.id = b.product_id " +
            "where p.id < :id and p.created_at >= :days order by b.score desc limit :size",
            nativeQuery = true)
    List<Product> findAllProductsBetween7DaysByCursorHasKeyOrderByScore(@Param("id") Long id, @Param("days")LocalDateTime days, @Param("size") int size);

    @Query(value = "select * from product as p left join best as b on p.id = b.product_id " +
            "where p.created_at >= :days order by b.score desc limit :size",
            nativeQuery = true)
    List<Product> findAllProductsBetween7DaysByCursorNoKeyOrderByScore(@Param("days") LocalDateTime days, @Param("size") int size);


    // 3. 낮은 가격순
    @Query(value = "select * from product where id < :id and created_at >= :days order by price asc limit :size",
            nativeQuery = true)
    List<Product> findAllProductsBetween7DaysByCursorHasKeyOrderByPriceAsc(@Param("id") Long id, @Param("days")LocalDateTime days, @Param("size") int size);

    @Query(value = "select * from product where created_at >= :days order by price asc limit :size", nativeQuery = true)
    List<Product> findAllProductsBetween7DaysByCursorNoKeyOrderByPriceAsc(@Param("days") LocalDateTime days, @Param("size") int size);

    // 4. 높은 가격순
    @Query(value = "select * from product where id < :id and created_at >= :days order by price desc limit :size",
            nativeQuery = true)
    List<Product> findAllProductsBetween7DaysByCursorHasKeyOrderByPriceDesc(@Param("id") Long id, @Param("days")LocalDateTime days, @Param("size") int size);

    @Query(value = "select * from product where created_at >= :days order by price desc limit :size", nativeQuery = true)
    List<Product> findAllProductsBetween7DaysByCursorNoKeyOrderByPriceDesc(@Param("days") LocalDateTime days, @Param("size") int size);

    // ==============================================================================================================
    // ===================================================== Best ===================================================
    // 1. 최신순

    // 2. 인기순

    // 3. 낮은 가격순

    // 4. 높은 가격순

    // ==============================================================================================================
    // ===================================================== Sale ==========================================a=========
    // 1. 최신순
    @Query(value = "select * from product where id < :id and saled = true order by id desc limit :size", nativeQuery = true)
    List<Product> findAllSaleProductsHasKey(@Param("id") Long id, @Param("size") int size);

    @Query(value = "select * from product where saled = true order by id desc limit :size", nativeQuery = true)
    List<Product> findAllSaleProductsNoKey(@Param("size") int size);

    // 2. 인기순
    @Query(value = "select * from product as p left join best as b on p.id = b.product_id " +
            "where p.id < :id and p.saled = true order by b.score desc limit :size;", nativeQuery = true)
    List<Product> findAllSaleProductsHasKeyOrderByScore(@Param("id") Long id, @Param("size") int size);

    @Query(value = "select * from product as p left join best as b on p.id = b.product_id " +
            "where p.saled = true order by b.score desc limit :size;", nativeQuery = true)
    List<Product> findAllSaleProductsNoKeyOrderByScore(@Param("size") int size);

    // 3. 낮은 가격순
    @Query(value = "select * from product where id < :id and saled = true order by price asc limit :size", nativeQuery = true)
    List<Product> findAllSaleProductsHasKeyOrderByPriceAsc(@Param("id") Long id, @Param("size") int size);

    @Query(value = "select * from product where saled = true order by price asc limit :size", nativeQuery = true)
    List<Product> findAllSaleProductsNoKeyOrderByPriceAsc(@Param("size") int size);

    // 4. 높은 가격순
    @Query(value = "select * from product where id < :id and saled = true order by price desc limit :size", nativeQuery = true)
    List<Product> findAllSaleProductsHasKeyOrderByPriceDesc(@Param("id") Long id, @Param("size") int size);

    @Query(value = "select * from product where saled = true order by price desc limit :size", nativeQuery = true)
    List<Product> findAllSaleProductsNoKeyOrderByPriceDesc(@Param("size") int size);
    // ===============================================================================================================

    // ======================= Clothes, Prop, Goods, HomeLivings, Beauty, Brand ======================================
    // categoryId = 0 일 때, 즉 탭별로 모든 상품 조회
    // 1. 최신순(기본)
    @Query(value = "select * from product where id < :id and type_id = :typeId order by id desc limit :size", nativeQuery = true)
    List<Product> findAllTypeProductsHasKey(@Param("id") Long id, @Param("typeId") Long typeId, @Param("size") int size);

    @Query(value = "select * from product where type_id = :typeId order by id desc limit :size", nativeQuery = true)
    List<Product> findAllTypeProductsNoKey(@Param("typeId") Long typeId, @Param("size") int size);


    // 탭별 카테고리 조회
    @Query(value = "select * from product where id < :id and type_id = :typeId and category_id = :categoryId " +
            "order by id desc limit :size", nativeQuery = true)
    List<Product> findTypeCategoryProductsHasKey(@Param("id") Long id,
                                            @Param("typeId") Long typeId,
                                            @Param("categoryId") Long categoryId,
                                            @Param("size") int size);

    @Query(value = "select * from product where type_id = :typeId and category_id = :categoryId " +
            "order by id desc limit :size", nativeQuery = true)
    List<Product> findTypeCategoryProductsNoKey(@Param("typeId") Long typeId,
                                           @Param("categoryId") Long categoryId,
                                           @Param("size") int size);

    // 2. 인기순
    @Query(value = "select * from product as p left join best as b on p.id = b.product_id " +
            "where p.id < :id and p.type_id = :typeId order by b.score desc limit :size;", nativeQuery = true)
    List<Product> findAllTypeProductsHasKeyOrderByScore(@Param("id") Long id, @Param("typeId") Long typeId, @Param("size") int size);

    @Query(value = "select * from product as p left join best as b on p.id = b.product_id " +
            "where p.type_id = :typeId order by b.score desc limit :size;", nativeQuery = true)
    List<Product> findAllTypeProductsNoKeyOrderByScore(@Param("typeId") Long typeId, @Param("size") int size);

    @Query(value = "select * from product as p left join best as b on p.id = b.product_id " +
            "where p.id < :id and p.type_id = :typeId and p.category_id = :categoryId " +
            "order by b.score desc limit :size", nativeQuery = true)
    List<Product> findTypeCategoryProductsHasKeyOrderByScore(@Param("id") Long id,
                                                             @Param("typeId") Long typeId,
                                                             @Param("categoryId") Long categoryId,
                                                             @Param("size") int size);

    @Query(value = "select * from product as p left join best as b on p.id = b.product_id " +
            "where p.type_id = :typeId and p.category_id = :categoryId order by b.score desc limit :size", nativeQuery = true)
    List<Product> findTypeCategoryProductsNoKeyOrderByScore(@Param("typeId") Long typeId,
                                                             @Param("categoryId") Long categoryId,
                                                             @Param("size") int size);

    // 3. 낮은 가격순
    @Query(value = "select * from product where id < :id and type_id = :typeId order by price asc limit :size", nativeQuery = true)
    List<Product> findAllTypeProductsHasKeyOrderByPriceAsc(@Param("id") Long id,
                                                           @Param("typeId") Long typeId,
                                                           @Param("size") int size);

    @Query(value = "select * from product where type_id = :typeId order by price asc limit :size", nativeQuery = true)
    List<Product> findAllTypeProductsNoKeyOrderByPriceAsc(@Param("typeId") Long typeId, @Param("size") int size);


    // 탭별 카테고리 조회
    @Query(value = "select * from product where id < :id and type_id = :typeId and category_id = :categoryId " +
            "order by price asc limit :size", nativeQuery = true)
    List<Product> findTypeCategoryProductsHasKeyOrderByPriceAsc(@Param("id") Long id,
                                                 @Param("typeId") Long typeId,
                                                 @Param("categoryId") Long categoryId,
                                                 @Param("size") int size);

    @Query(value = "select * from product where type_id = :typeId and category_id = :categoryId " +
            "order by price asc limit :size", nativeQuery = true)
    List<Product> findTypeCategoryProductsNoKeyOrderByPriceAsc(@Param("typeId") Long typeId,
                                                @Param("categoryId") Long categoryId,
                                                @Param("size") int size);

    // 4. 높은 가격순
    @Query(value = "select * from product where id < :id and type_id = :typeId order by price desc limit :size", nativeQuery = true)
    List<Product> findAllTypeProductsHasKeyOrderByPriceDesc(@Param("id") Long id,
                                                            @Param("typeId") Long typeId,
                                                            @Param("size") int size);

    @Query(value = "select * from product where type_id = :typeId order by price desc limit :size", nativeQuery = true)
    List<Product> findAllTypeProductsNoKeyOrderByPriceDesc(@Param("typeId") Long typeId, @Param("size") int size);

    // 탭별 카테고리 조회
    @Query(value = "select * from product where id < :id and type_id = :typeId and category_id = :categoryId " +
            "order by price desc limit :size", nativeQuery = true)
    List<Product> findTypeCategoryProductsHasKeyOrderByPriceDesc(@Param("id") Long id,
                                                 @Param("typeId") Long typeId,
                                                 @Param("categoryId") Long categoryId,
                                                 @Param("size") int size);

    @Query(value = "select * from product where type_id = :typeId and category_id = :categoryId " +
            "order by price desc limit :size", nativeQuery = true)
    List<Product> findTypeCategoryProductsNoKeyOrderByPriceDesc(@Param("typeId") Long typeId,
                                                @Param("categoryId") Long categoryId,
                                                @Param("size") int size);
    // ==============================================================================================================

}
