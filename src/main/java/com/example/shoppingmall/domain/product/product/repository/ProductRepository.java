package com.example.shoppingmall.domain.product.product.repository;

import com.example.shoppingmall.domain.brand.util.CategoryIdsMapping;
import com.example.shoppingmall.domain.product.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Product findProductById(Long id);
    Product findTopByOrderByPriceDesc();

    List<Product> findProductsByIdIn(List<Long> productIds);

    //============================================= 전체 상품 조회 =====================================================
    // 0. 기본 최신순
    @Query(value = "select * from product where id < :id order by id desc limit :size", nativeQuery = true)
    List<Product> findAllOrderByIdDescHasKey(@Param("id") Long id, @Param("size") int size);
    @Query(value = "select * from product order by id desc limit :size", nativeQuery = true)
    List<Product> findAllOrderByIdDescNoKey(@Param("size") int size);

    // 1. 인기순
    //    상품 점수 필드 생성(하루마다 업데이트)
    @Query(value = "select * from product as p left join best_product as bp on p.id = bp.product_id " +
            "where bp.score < :score order by bp.score desc limit :size", nativeQuery = true)
    List<Product> findAllOrderByScoreHasKey(@Param("score") Double score, @Param("size") int size);
    @Query(value = "select * from product as p left join best_product as bp on p.id = bp.product_id " +
            "order by bp.score desc limit :size", nativeQuery = true)
    List<Product> findAllOrderByScoreNoKey(@Param("size") int size);

    // 2. 낮은 가격순
    @Query(value = "select * from product where price > :price order by price asc limit :size", nativeQuery = true)
    List<Product> findAllOrderByPriceAscHasKey(@Param("price") Integer price, @Param("size") int size);
    @Query(value = "select * from product order by price asc limit :size", nativeQuery = true)
    List<Product> findAllOrderByPriceAscNoKey(@Param("size") int size);

    // 3. 높은 가격순
    @Query(value = "select * from product where price < :price order by price desc limit :size", nativeQuery = true)
    List<Product> findAllOrderByPriceDescHasKey(@Param("price") Integer price, @Param("size") int size);
    @Query(value = "select * from product order by price desc limit :size", nativeQuery = true)
    List<Product> findAllOrderByPriceDescNoKey(@Param("size") int size);
    // ==============================================================================================================

    // ====================================================== New ===================================================
    // 1. 기본(최신순)
    @Query(value = "select * from product where id < :id and created_at >= :days order by id desc limit :size", nativeQuery = true)
    List<Product> findAllNewOrderByIdDescHasKey(@Param("id") Long id, @Param("size") int size, @Param("days")LocalDateTime days);
    @Query(value = "select * from product where created_at >= :days order by id desc limit :size", nativeQuery = true)
    List<Product> findAllNewOrderByIdDescNoKey(@Param("size") int size, @Param("days") LocalDateTime days);

    // 2. 인기순
    @Query(value = "select * from product as p left join best_product as bp on p.id = bp.product_id " +
            "where bp.score < :score and p.created_at >= :days order by bp.score desc limit :size", nativeQuery = true)
    List<Product> findAllNewOrderByScoreHasKey(@Param("score") Double score,  @Param("size") int size, @Param("days")LocalDateTime days);
    @Query(value = "select * from product as p left join best_product as bp on p.id = bp.product_id " +
            "where p.created_at >= :days order by bp.score desc limit :size", nativeQuery = true)
    List<Product> findAllNewOrderByScoreNoKey(@Param("size") int size, @Param("days") LocalDateTime days);

    // 3. 낮은 가격순
    @Query(value = "select * from product where price > :price and created_at >= :days order by price asc limit :size", nativeQuery = true)
    List<Product> findAllNewOrderByPriceAscHasKey(@Param("price") Integer price, @Param("size") int size, @Param("days")LocalDateTime days);
    @Query(value = "select * from product where created_at >= :days order by price asc limit :size", nativeQuery = true)
    List<Product> findAllNewOrderByPriceAscNoKey(@Param("size") int size, @Param("days") LocalDateTime days);

    // 4. 높은 가격순
    @Query(value = "select * from product where price < :price and created_at >= :days order by price desc limit :size", nativeQuery = true)
    List<Product> findAllNewOrderByPriceDescHasKey(@Param("price") Integer price, @Param("size") int size, @Param("days")LocalDateTime days);
    @Query(value = "select * from product where created_at >= :days order by price desc limit :size", nativeQuery = true)
    List<Product> findAllNewOrderByPriceDescNoKey(@Param("size") int size, @Param("days") LocalDateTime days);
    // ==============================================================================================================
    // ===================================================== Best ===================================================
//    @Query(value = "select * from product as p left join clothes_product as cp on p.id = cp.product_id " +
//            "where p.type_id = :typeId and cp.category_id = :categoryId order by stock desc limit 3", nativeQuery = true)
//    List<Product> findTop3ByTypeIdAndCategoryIdOrderByStockDesc(Long typeId, Long categoryId);
//    List<Product> findTop3ByTypeIdOrderByStockDesc(Long typeId);
    // ==============================================================================================================
    // ===================================================== Sale ==========================================a=========
    // 1. 최신순
    @Query(value = "select * from product where id < :id and saled = true order by id desc limit :size", nativeQuery = true)
    List<Product> findAllSaleOrderByIdDescHasKey(@Param("id") Long id, @Param("size") int size);
    @Query(value = "select * from product where saled = true order by id desc limit :size", nativeQuery = true)
    List<Product> findAllSaleOrderByIdDescNoKey(@Param("size") int size);

    // 2. 인기순
    @Query(value = "select * from product as p left join best_product as bp on p.id = bp.product_id " +
            "where bp.score < :score and p.saled = true order by bp.score desc limit :size;", nativeQuery = true)
    List<Product> findAllSaleOrderByScoreHasKey(@Param("score") Double score, @Param("size") int size);
    @Query(value = "select * from product as p left join best_product as bp on p.id = bp.product_id " +
            "where p.saled = true order by bp.score desc limit :size;", nativeQuery = true)
    List<Product> findAllSaleOrderByScoreNoKey(@Param("size") int size);

    // 3. 낮은 가격순
    @Query(value = "select * from product where price > :price and saled = true order by price asc limit :size", nativeQuery = true)
    List<Product> findAllSaleOrderByPriceAscHasKey(@Param("price") Integer price, @Param("size") int size);
    @Query(value = "select * from product where saled = true order by price asc limit :size", nativeQuery = true)
    List<Product> findAllSaleOrderByPriceAscNoKey(@Param("size") int size);

    // 4. 높은 가격순
    @Query(value = "select * from product where price < :price and saled = true order by price desc limit :size", nativeQuery = true)
    List<Product> findAllSaleOrderByPriceDescHasKey(@Param("price") Integer price, @Param("size") int size);
    @Query(value = "select * from product where saled = true order by price desc limit :size", nativeQuery = true)
    List<Product> findAllSaleOrderByPriceDescNoKey(@Param("size") int size);
    // ===============================================================================================================

    // =============================================== 카테고리별 상품 조회 ===============================================
    // subCategoryId = 0: 카테고리별 모든 상품 조회
    // 1. 최신순(기본)
    @Query(value = "select * from product where id < :id and category_id = :categoryId order by id desc limit :size", nativeQuery = true)
    List<Product> findAllByCategoryIdOrderByIdDescHasKey(@Param("id") Long id,
                                                         @Param("categoryId") Long categoryId,
                                                         @Param("size") int size);

    @Query(value = "select * from product where category_id = :categoryId order by id desc limit :size", nativeQuery = true)
    List<Product> findAllByCategoryIdOrderByIdDescNoKey(@Param("categoryId") Long categoryId,
                                                        @Param("size") int size);

    @Query(value = "select * from product where id < :id and category_id = :categoryId and sub_category_id = :subCategoryId order by id desc limit :size", nativeQuery = true)
    List<Product> findAllByCategoryAndSubCategoryIdOrderByIdDescHasKey(@Param("id") Long id,
                                                                       @Param("categoryId") Long categoryId,
                                                                       @Param("subCategoryId") Long subCategoryId,
                                                                       @Param("size") int size);

    @Query(value = "select * from product where category_id = :categoryId and sub_category_id = :subCategoryId order by id desc limit :size", nativeQuery = true)
    List<Product> findAllByCategoryAndSubCategoryIdOrderByIdDescNoKey(@Param("categoryId") Long categoryId,
                                                                      @Param("subCategoryId") Long subCategoryId,
                                                                      @Param("size") int size);

    // 2. 인기순
    @Query(value = "select * from product as p left join best_product as bp on p.id = bp.product_id " +
            "where bp.score < :score and p.category_id = :categoryId order by bp.score desc limit :size;", nativeQuery = true)
    List<Product> findAllByCategoryIdOrderByScoreHasKey(@Param("score") Double score,
                                                        @Param("categoryId") Long categoryId,
                                                        @Param("size") int size);

    @Query(value = "select * from product as p left join best_product as bp on p.id = bp.product_id " +
            "where p.category_id = :categoryId order by bp.score desc limit :size;", nativeQuery = true)
    List<Product> findAllByCategoryIdOrderByScoreNoKey(@Param("categoryId") Long categoryId,
                                                       @Param("size") int size);

    @Query(value = "select * from product as p left join best_product as bp on p.id = bp.product_id " +
            "where bp.score < :score and p.category_id = :categoryId and p.sub_category_id = :subCategoryId order by bp.score desc limit :size", nativeQuery = true)
    List<Product> findAllByCategoryAndSubCategoryIdOrderByScoreHasKey(@Param("score") Double score,
                                                                      @Param("categoryId") Long categoryId,
                                                                      @Param("subCategoryId") Long subCategoryId,
                                                                      @Param("size") int size);

    @Query(value = "select * from product as p left join best_product as bp on p.id = bp.product_id " +
            "where p.category_id = :categoryId and sub_category_id = :subCategoryId order by bp.score desc limit :size", nativeQuery = true)
    List<Product> findAllByCategoryAndSubCategoryIdOrderByScoreNoKey(@Param("categoryId") Long categoryId,
                                                                     @Param("subCategoryId") Long subCategoryId,
                                                                     @Param("size") int size);

    // 3. 낮은 가격순
    @Query(value = "select * from product where price > :price and category_id = :categoryId order by price asc limit :size", nativeQuery = true)
    List<Product> findAllByCategoryIdOrderByPriceAscHasKey(@Param("price") Integer price,
                                                           @Param("categoryId") Long categoryId,
                                                           @Param("size") int size);

    @Query(value = "select * from product where category_id = :categoryId order by price asc limit :size", nativeQuery = true)
    List<Product> findAllByCategoryIdOrderByPriceAscNoKey(@Param("categoryId") Long categoryId,
                                                          @Param("size") int size);

    @Query(value = "select * from product where price > :price and category_id = :categoryId and sub_category_id = :subCategoryId order by price asc limit :size", nativeQuery = true)
    List<Product> findAllByCategoryAndSubCategoryIdOrderByPriceAscHasKey(@Param("price") Integer price,
                                                                         @Param("categoryId") Long categoryId,
                                                                         @Param("subCategoryId") Long subCategoryId,
                                                                         @Param("size") int size);

    @Query(value = "select * from product where category_id = :categoryId and sub_category_id = :subCategoryId order by price asc limit :size", nativeQuery = true)
    List<Product> findAllByCategoryAndSubCategoryIdOrderByPriceAscNoKey(@Param("categoryId") Long categoryId,
                                                                        @Param("subCategoryId") Long subCategoryId,
                                                                        @Param("size") int size);

    // 4. 높은 가격순
    @Query(value = "select * from product where price < :price and category_id = :categoryId order by price desc limit :size", nativeQuery = true)
    List<Product> findAllByCategoryIdOrderByPriceDescHasKey(@Param("price") Integer price,
                                                            @Param("categoryId") Long categoryId,
                                                            @Param("size") int size);

    @Query(value = "select * from product where category_id = :categoryId order by price desc limit :size", nativeQuery = true)
    List<Product> findAllByCategoryIdOrderByPriceDescNoKey(@Param("categoryId") Long categoryId,
                                                           @Param("size") int size);

    @Query(value = "select * from product where price < :price and category_id = :categoryId and sub_category_id = :subCategoryId order by price desc limit :size", nativeQuery = true)
    List<Product> findAllByCategoryAndSubCategoryIdOrderByPriceDescHasKey(@Param("price") Integer price,
                                                                          @Param("categoryId") Long categoryId,
                                                                          @Param("subCategoryId") Long subCategoryId,
                                                                          @Param("size") int size);

    @Query(value = "select * from product where category_id = :categoryId and sub_category_id = :subCategoryId order by price desc limit :size", nativeQuery = true)
    List<Product> findAllByCategoryAndSubCategoryIdOrderByPriceDescNoKey(@Param("categoryId") Long categoryId,
                                                                         @Param("subCategoryId") Long subCategoryId,
                                                                         @Param("size") int size);
    // ==============================================================================================================
    // ============================== Brand 별 상품 조회 ===========================================================

    // 1. 최신순(HasKey)
    @Query(value = "select * from product as p left join brand_product as bp on p.id = bp.product_id " +
            "where p.id < :id and bp.brand_id = :brandId order by p.id desc limit :size", nativeQuery = true)
    List<Product> findAllByBrandOrderByIdDescHasKey(@Param("id") Long id, @Param("brandId") Long brandId, @Param("size") int size);

    @Query(value = "select * from product as p left join brand_product as bp on p.id = bp.product_id " +
            "where bp.brand_id = :brandId order by p.id desc limit :size", nativeQuery = true)
    List<Product> findAllByBrandOrderByIdDescNoKey(@Param("brandId") Long brandId, @Param("size") int size);

    // 2. 인기순(HasKey)
    @Query(value = "select * from product as p left join brand_product as bp on p.id = bp.product_id " +
            "left join best_product as bep on p.id = bep.product_id where bep.score < :score and " +
            "bp.brand_id = :brandId order by bep.score desc limit :size", nativeQuery = true)
    List<Product> findAllByBrandOrderByScoreHasKey(@Param("score") Double score, @Param("brandId") Long brandId, @Param("size") int size);

    @Query(value = "select * from product as p left join brand_product as bp on p.id = bp.product_id " +
            "left join best_product as bep on p.id = bep.product_id where bp.brand_id = :brandId order by bep.score desc limit :size", nativeQuery = true)
    List<Product> findAllByBrandOrderByScoreNoKey(@Param("brandId") Long brandId, @Param("size") int size);

    // 3. 가격 낮은순(HasKey)
    @Query(value = "select * from product as p left join brand_product as bp on p.id = bp.product_id " +
            "where p.price > :price and bp.brand_id = :brandId order by p.price asc limit :size", nativeQuery = true)
    List<Product> findAllByBrandOrderByPriceAscHasKey(@Param("price") Integer price, @Param("brandId") Long brandId, @Param("size") int size);

    @Query(value = "select * from product as p left join brand_product as bp on p.id = bp.product_id " +
            "where bp.brand_id = :brandId order by p.price asc limit :size", nativeQuery = true)
    List<Product> findAllByBrandOrderByPriceAscNoKey(@Param("brandId") Long brandId, @Param("size") int size);

    // 4. 가격 높은순(HasKey)
    @Query(value = "select * from product as p left join brand_product as bp on p.id = bp.product_id " +
            "where p.price < :price and bp.brand_id = :brandId order by p.price desc limit :size", nativeQuery = true)
    List<Product> findAllByBrandOrderByPriceDescHasKey(@Param("price") Integer price, @Param("brandId") Long brandId, @Param("size") int size);
    //    가격 높은순(NoKey)
    @Query(value = "select * from product as p left join brand_product as bp on p.id = bp.product_id " +
            "where bp.brand_id = :brandId order by p.price desc limit :size", nativeQuery = true)
    List<Product> findAllByBrandOrderByPriceDescNoKey(@Param("brandId") Long brandId, @Param("size") int size);



    @Query(value = "select * from product as p left join brand_product as bp on p.id = bp.product_id where p.id < :id " +
            "and bp.brand_id = :brandId and p.category_id = :categoryId and p.sub_category_id = :subCategoryId order by id desc limit :size", nativeQuery = true)
    List<Product> findAllByBrandCategoryOrderByIdDescHasKey(@Param("id") Long id,
                                                            @Param("brandId") Long brandId,
                                                            @Param("categoryId") Long categoryId,
                                                            @Param("subCategoryId") Long subCategoryId,
                                                            @Param("size") int size);

    @Query(value = "select * from product as p left join brand_product as bp on p.id = bp.product_id where bp.brand_id = :brandId " +
            "and p.category_id = :categoryId and p.sub_category_id = :subCategoryId order by id desc limit :size", nativeQuery = true)
    List<Product> findAllByBrandCategoryOrderByIdDescNoKey(@Param("brandId") Long brandId,
                                                               @Param("categoryId") Long categoryId,
                                                               @Param("subCategoryId") Long subCategoryId,
                                                               @Param("size") int size);

    // 2. 인기순(HasKey)
    @Query(value = "select * from product as p left join brand_product as bp on p.id = bp.product_id left join best_product as bep " +
            "on p.id = bep.product_id where bep.score < :score and bp.brand_id = :brandId and p.category_id = :categoryId" +
            " and p.sub_category_id = :subCategoryId order by bep.score desc limit :size", nativeQuery = true)
    List<Product> findAllByBrandCategoryOrderByScoreHasKey(@Param("score") Double score,
                                                           @Param("brandId") Long brandId,
                                                           @Param("categoryId") Long categoryId,
                                                           @Param("subCategoryId") Long subCategoryId,
                                                           @Param("size") int size);

    @Query(value = "select * from product as p left join brand_product as bp on p.id = bp.product_id left join best_product as bep " +
            "on p.id = bep.product_id where bp.brand_id = :brandId and p.category_id = :categoryId" +
            " and p.sub_category_id = :subCategoryId order by bep.score desc limit :size", nativeQuery = true)
    List<Product> findAllByBrandCategoryOrderByScoreNoKey(@Param("brandId") Long brandId,
                                                          @Param("categoryId") Long categoryId,
                                                          @Param("subCategoryId") Long subCategoryId,
                                                          @Param("size") int size);

    // 3. 가격 낮은순(HasKey)
    @Query(value = "select * from product as p left join brand_product as bp on p.id = bp.product_id " +
            "where p.price > :price and bp.brand_id = :brandId and p.category_id = :categoryId " +
            "and p.sub_category_id = :subCategoryId order by p.price asc limit :size", nativeQuery = true)
    List<Product> findAllByBrandCategoryOrderByPriceAscHasKey(@Param("price") Integer price,
                                                              @Param("brandId") Long brandId,
                                                              @Param("categoryId") Long categoryId,
                                                              @Param("subCategoryId") Long subCategoryId,
                                                              @Param("size") int size);
    @Query(value = "select * from product as p left join brand_product as bp on p.id = bp.product_id " +
            "where bp.brand_id = :brandId and p.category_id = :categoryId and p.sub_category_id = :subCategoryId " +
            "order by p.price asc limit :size", nativeQuery = true)
    List<Product> findAllByBrandCategoryOrderByPriceAscNoKey(@Param("brandId") Long brandId,
                                                             @Param("categoryId") Long categoryId,
                                                             @Param("subCategoryId") Long subCategoryId,
                                                             @Param("size") int size);

    // 4. 가격 높은순(HasKey)
    @Query(value = "select * from product as p left join brand_product as bp on p.id = bp.product_id " +
            "where p.price < :price and bp.brand_id = :brandId and p.category_id = :categoryId " +
            "and p.sub_category_id = :subCategoryId order by p.price desc limit :size", nativeQuery = true)
    List<Product> findAllByBrandCategoryOrderByPriceDescHasKey(@Param("price") Integer price,
                                                               @Param("brandId") Long brandId,
                                                               @Param("categoryId") Long categoryId,
                                                               @Param("subCategoryId") Long subCategoryId,
                                                               @Param("size") int size);

    @Query(value = "select * from product as p left join brand_product as bp on p.id = bp.product_id " +
            "where bp.brand_id = :brandId and p.category_id = :categoryId and p.sub_category_id = :subCategoryId " +
            "order by p.price desc limit :size", nativeQuery = true)
    List<Product> findAllByBrandCategoryOrderByPriceDescNoKey(@Param("brandId") Long brandId,
                                                              @Param("categoryId") Long categoryId,
                                                              @Param("subCategoryId") Long subCategoryId,
                                                              @Param("size") int size);

    //==============================================================================
    @Query(value = "select bp.score from product as p " +
            "left join best_product as bp on p.id = bp.product_id " +
            "where bp.product_id = :productId", nativeQuery = true)
    Double findProductScore(@Param("productId") Long productId);


    @Query(value = "select distinct p.category_id as categoryId, p.sub_category_id as subCategoryId from product as p left join brand_product as bp " +
            "on p.id = bp.product_id where bp.brand_id = :brandId order by p.category_id asc, p.sub_category_id asc", nativeQuery = true)
    List<CategoryIdsMapping> findCategoryIdsByBrandId(@Param("brandId") Long brandId);
}
