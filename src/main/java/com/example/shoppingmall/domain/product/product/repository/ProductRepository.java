package com.example.shoppingmall.domain.product.product.repository;

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
    List<Product> findAllProductsByCursorHasKey(@Param("id") Long id, @Param("size") int size);

    @Query(value = "select * from product order by id desc limit :size", nativeQuery = true)
    List<Product> findAllProductsByCursorNoKey(@Param("size") int size);

    // 1. 인기순
    //    상품 점수 필드 생성(하루마다 업데이트)
    @Query(value = "select * from product as p left join best as b on p.id = b.product_id " +
            "where b.score < :score order by b.score desc limit :size", nativeQuery = true)
    List<Product> findAllProductsByCursorHasKeyOrderByScore(@Param("score") Double score, @Param("size") int size);

    @Query(value = "select * from product as p left join best as b on p.id = b.product_id " +
            "order by b.score desc limit :size", nativeQuery = true)
    List<Product> findAllProductsByCursorNoKeyOrderByScore(@Param("size") int size);

    // 2. 낮은 가격순
    @Query(value = "select * from product where price < :price order by price asc limit :size", nativeQuery = true)
    List<Product> findAllProductsByCursorHasKeyOrderByPriceAsc(@Param("price") Integer price, @Param("size") int size);

    @Query(value = "select * from product order by price asc limit :size", nativeQuery = true)
    List<Product> findAllProductsByCursorNoKeyOrderByPriceAsc(@Param("size") int size);

    // 3. 높은 가격순
    @Query(value = "select * from product where price < :price order by price desc limit :size", nativeQuery = true)
    List<Product> findAllProductsByCursorHasKeyOrderByPriceDesc(@Param("price") Integer price, @Param("size") int size);

    @Query(value = "select * from product order by price desc limit :size", nativeQuery = true)
    List<Product> findAllProductsByCursorNoKeyOrderByPriceDesc(@Param("size") int size);

    // ==============================================================================================================

    // ====================================================== New ===================================================
    // 1. 기본(최신순)
    @Query(value = "select * from product where id < :id and created_at >= :days " +
            "order by id desc limit :size",
            nativeQuery = true)
    List<Product> findAllNewProductsByCursorHasKey(@Param("id") Long id, @Param("size") int size, @Param("days")LocalDateTime days);

    @Query(value = "select * from product where created_at >= :days " +
            "order by id desc limit :size", nativeQuery = true)
    List<Product> findAllNewProductsByCursorNoKey(@Param("size") int size, @Param("days") LocalDateTime days);

    // 2. 인기순
    @Query(value = "select * from product as p " +
            "left join best as b on p.id = b.product_id " +
            "where b.score < :score and p.created_at >= :days " +
            "order by b.score desc limit :size", nativeQuery = true)
    List<Product> findAllNewProductsByCursorHasKeyOrderByScore(@Param("score") Double score,  @Param("size") int size, @Param("days")LocalDateTime days);

    @Query(value = "select * from product as p " +
            "left join best as b on p.id = b.product_id " +
            "where p.created_at >= :days " +
            "order by b.score desc limit :size", nativeQuery = true)
    List<Product> findAllNewProductsByCursorNoKeyOrderByScore(@Param("size") int size, @Param("days") LocalDateTime days);


    // 3. 낮은 가격순
    @Query(value = "select * from product where price < :price and created_at >= :days order by price asc limit :size", nativeQuery = true)
    List<Product> findAllNewProductsByCursorHasKeyOrderByPriceAsc(@Param("price") Integer price, @Param("days")LocalDateTime days, @Param("size") int size);

    @Query(value = "select * from product where created_at >= :days order by price asc limit :size", nativeQuery = true)
    List<Product> findAllNewProductsByCursorNoKeyOrderByPriceAsc(@Param("days") LocalDateTime days, @Param("size") int size);

    // 4. 높은 가격순
    @Query(value = "select * from product where price < :price and created_at >= :days order by price desc limit :size", nativeQuery = true)
    List<Product> findAllNewProductsByCursorHasKeyOrderByPriceDesc(@Param("price") Integer price, @Param("days")LocalDateTime days, @Param("size") int size);

    @Query(value = "select * from product where created_at >= :days order by price desc limit :size", nativeQuery = true)
    List<Product> findAllNewProductsByCursorNoKeyOrderByPriceDesc(@Param("days") LocalDateTime days, @Param("size") int size);

    // ==============================================================================================================
    // ===================================================== Best ===================================================
    @Query(value = "select * from product as p left join clothes_product as cp on p.id = cp.product_id " +
            "where p.type_id = :typeId and cp.category_id = :categoryId order by stock desc limit 3", nativeQuery = true)
    List<Product> findTop3ByTypeIdAndCategoryIdOrderByStockDesc(Long typeId, Long categoryId);
    List<Product> findTop3ByTypeIdOrderByStockDesc(Long typeId);
    // ==============================================================================================================
    // ===================================================== Sale ==========================================a=========
    // 1. 최신순
    @Query(value = "select * from product where id < :id and saled = true order by id desc limit :size", nativeQuery = true)
    List<Product> findAllSaleProductsHasKey(@Param("id") Long id, @Param("size") int size);

    @Query(value = "select * from product where saled = true order by id desc limit :size", nativeQuery = true)
    List<Product> findAllSaleProductsNoKey(@Param("size") int size);

    // 2. 인기순
    @Query(value = "select * from product as p left join best as b on p.id = b.product_id " +
            "where b.score < :score and p.saled = true order by b.score desc limit :size;", nativeQuery = true)
    List<Product> findAllSaleProductsHasKeyOrderByScore(@Param("score") Double score, @Param("size") int size);

    @Query(value = "select * from product as p left join best as b on p.id = b.product_id " +
            "where p.saled = true order by b.score desc limit :size;", nativeQuery = true)
    List<Product> findAllSaleProductsNoKeyOrderByScore(@Param("size") int size);

    // 3. 낮은 가격순
    @Query(value = "select * from product where price < :price and saled = true order by price asc limit :size", nativeQuery = true)
    List<Product> findAllSaleProductsHasKeyOrderByPriceAsc(@Param("price") Integer price, @Param("size") int size);

    @Query(value = "select * from product where saled = true order by price asc limit :size", nativeQuery = true)
    List<Product> findAllSaleProductsNoKeyOrderByPriceAsc(@Param("size") int size);

    // 4. 높은 가격순
    @Query(value = "select * from product where price < :price and saled = true order by price desc limit :size", nativeQuery = true)
    List<Product> findAllSaleProductsHasKeyOrderByPriceDesc(@Param("price") Integer price, @Param("size") int size);

    @Query(value = "select * from product where saled = true order by price desc limit :size", nativeQuery = true)
    List<Product> findAllSaleProductsNoKeyOrderByPriceDesc(@Param("size") int size);
    // ===============================================================================================================

    // ======================= 의류 ======================================
    // categoryId = 0 일 때, 즉 탭별로 모든 상품 조회
    // 1. 최신순(기본)
    @Query(value = "select * from product where type_id = :typeId and id < :id order by id desc limit :size", nativeQuery = true)
    List<Product> findAllTypeProductsHasKey(@Param("id") Long id, @Param("typeId") Long typeId, @Param("size") int size);

    @Query(value = "select * from product where type_id = :typeId order by id desc limit :size", nativeQuery = true)
    List<Product> findAllTypeProductsNoKey(@Param("typeId") Long typeId, @Param("size") int size);


    // 탭별 카테고리 조회
    @Query(value = "select * from product as p left join clothes_product as cp on p.id = cp.product_id " +
            "where p.id < :id and p.type_id = :typeId and cp.category_id = :categoryId order by id desc limit :size", nativeQuery = true)
    List<Product> findAllTypeCategoryProductsHasKey(@Param("id") Long id,
                                                @Param("typeId") Long typeId,
                                                @Param("categoryId") Long categoryId,
                                                @Param("size") int size);

    @Query(value = "select * from product as p left join clothes_product as cp on p.id = cp.product_id " +
            "where p.type_id = :typeId and cp.category_id = :categoryId order by id desc limit :size", nativeQuery = true)
    List<Product> findAllTypeCategoryProductsNoKey(@Param("typeId") Long typeId,
                                           @Param("categoryId") Long categoryId,
                                           @Param("size") int size);

    // 2. 인기순
    @Query(value = "select * from product as p " +
            "left join best as b on p.id = b.product_id " +
            "where b.score < :score and p.type_id = :typeId order by b.score desc limit :size;", nativeQuery = true)
    List<Product> findAllTypeProductsHasKeyOrderByScore(@Param("score") Double score, @Param("typeId") Long typeId, @Param("size") int size);

    @Query(value = "select * from product as p " +
            "left join best as b on p.id = b.product_id " +
            "where p.type_id = :typeId order by b.score desc limit :size;", nativeQuery = true)
    List<Product> findAllTypeProductsNoKeyOrderByScore(@Param("typeId") Long typeId, @Param("size") int size);

    @Query(value = "select * from product as p " +
            "left join best as b on p.id = b.product_id " +
            "left join clothes_product as cp on p.id = cp.product_id " +
            "where b.score < :score and p.type_id = :typeId and cp.category_id = :categoryId order by b.score desc limit :size", nativeQuery = true)
    List<Product> findTypeCategoryProductsHasKeyOrderByScore(@Param("score") Double score,
                                                             @Param("typeId") Long typeId,
                                                             @Param("categoryId") Long categoryId,
                                                             @Param("size") int size);

    @Query(value = "select * from product as p " +
            "left join best as b on p.id = b.product_id " +
            "left join clothes_product as cp on p.id = cp.product_id " +
            "where p.type_id = :typeId and cp.category_id = :categoryId order by b.score desc limit :size", nativeQuery = true)
    List<Product> findTypeCategoryProductsNoKeyOrderByScore(@Param("typeId") Long typeId,
                                                             @Param("categoryId") Long categoryId,
                                                             @Param("size") int size);

    // 3. 낮은 가격순
    @Query(value = "select * from product where price < :price and type_id = :typeId order by price asc limit :size", nativeQuery = true)
    List<Product> findAllTypeProductsHasKeyOrderByPriceAsc(@Param("price") Integer price, @Param("typeId") Long typeId, @Param("size") int size);

    @Query(value = "select * from product where type_id = :typeId order by price asc limit :size", nativeQuery = true)
    List<Product> findAllTypeProductsNoKeyOrderByPriceAsc(@Param("typeId") Long typeId, @Param("size") int size);

    // 탭별 카테고리 조회
    @Query(value = "select * from product as p " +
            "left join clothes_product as cp on p.id = cp.product_id " +
            "where p.price < :price and p.type_id = :typeId and cp.category_id = :categoryId order by p.price asc limit :size", nativeQuery = true)
    List<Product> findTypeCategoryProductsHasKeyOrderByPriceAsc(@Param("price") Integer price,
                                                 @Param("typeId") Long typeId,
                                                 @Param("categoryId") Long categoryId,
                                                 @Param("size") int size);

    @Query(value = "select * from product as p " +
            "left join clothes_product as cp on p.id = cp.product_id " +
            "where p.type_id = :typeId and cp.category_id = :categoryId order by p.price asc limit :size", nativeQuery = true)
    List<Product> findTypeCategoryProductsNoKeyOrderByPriceAsc(@Param("typeId") Long typeId,
                                                @Param("categoryId") Long categoryId,
                                                @Param("size") int size);

    // 4. 높은 가격순
    @Query(value = "select * from product where price < :price and type_id = :typeId order by price desc limit :size", nativeQuery = true)
    List<Product> findAllTypeProductsHasKeyOrderByPriceDesc(@Param("price") Integer price,
                                                            @Param("typeId") Long typeId,
                                                            @Param("size") int size);

    @Query(value = "select * from product where type_id = :typeId order by price desc limit :size", nativeQuery = true)
    List<Product> findAllTypeProductsNoKeyOrderByPriceDesc(@Param("typeId") Long typeId, @Param("size") int size);

    // 탭별 카테고리 조회
    @Query(value = "select * from product as p " +
            "left join clothes_product as cp on p.id = cp.product_id " +
            "where p.price < :price and p.type_id = :typeId and cp.category_id = :categoryId order by p.price desc limit :size", nativeQuery = true)
    List<Product> findTypeCategoryProductsHasKeyOrderByPriceDesc(@Param("price") Integer price,
                                                 @Param("typeId") Long typeId,
                                                 @Param("categoryId") Long categoryId,
                                                 @Param("size") int size);

    @Query(value = "select * from product as p " +
            "left join clothes_product as cp on p.id = cp.product_id " +
            "where p.type_id = :typeId and cp.category_id = :categoryId order by p.price desc limit :size", nativeQuery = true)
    List<Product> findTypeCategoryProductsNoKeyOrderByPriceDesc(@Param("typeId") Long typeId,
                                                @Param("categoryId") Long categoryId,
                                                @Param("size") int size);
    // ==============================================================================================================
    // ============================== Brand 별 상품 조회 ===========================================================
    // 1. brandId
    // 2. categoryId

    // 1. 최신순(HasKey)
    @Query(value = "select p.* from product as p " +
            "left join product_brand_category as pbc " +
            "on p.id = pbc.product_id " +
            "where pbc.brand_id = :brandId and p.id < :id " +
            "order by p.id desc limit :size", nativeQuery = true)
    List<Product> findBrandProductsByBrandIdHasKey(@Param("id") Long id,
                                                                        @Param("brandId") Long brandId,
                                                                        @Param("size") int size);
    //    최신순(NoKey)
    @Query(value = "select p.* from product as p " +
            "left join product_brand_category as pbc " +
            "on p.id = pbc.product_id " +
            "where pbc.brand_id = :brandId " +
            "order by p.id desc limit :size", nativeQuery = true)
    List<Product> findBrandProductsByBrandIdNoKey(@Param("brandId") Long brandId,
                                                                       @Param("size") int size);

    // 2. 인기순(HasKey)
    @Query(value = "select p.* from product as p " +
            "left join product_brand_category as pbc on p.id = pbc.product_id " +
            "left join best as b on p.id = b.product_id " +
            "where pbc.brand_id = :brandId and b.score < :score " +
            "order by b.score desc limit :size", nativeQuery = true)
    List<Product> findBrandProductsByBrandIdHasKeyOrderByScore(@Param("score") Double score,
                                                                       @Param("brandId") Long brandId,
                                                                       @Param("size") int size);
    //    인기순(NoKey)
    @Query(value = "select p.* from product as p " +
            "left join product_brand_category as pbc on p.id = pbc.product_id " +
            "left join best as b on p.id = b.product_id " +
            "where pbc.brand_id = :brandId " +
            "order by b.score desc limit :size", nativeQuery = true)
    List<Product> findBrandProductsByBrandIdNoKeyOrderByScore(@Param("brandId") Long brandId,
                                                                      @Param("size") int size);

    // 3. 가격 낮은순(HasKey)
    @Query(value = "select p.* from product as p " +
            "left join product_brand_category as pbc " +
            "on p.id = pbc.product_id " +
            "where pbc.brand_id = :brandId and p.price < :price " +
            "order by p.price asc limit :size", nativeQuery = true)
    List<Product> findBrandProductsByBrandIdHasKeyOrderByPriceAsc(@Param("price") Integer price,
                                                                 @Param("brandId") Long brandId,
                                                                 @Param("size") int size);
    //    가격 낮은순(NoKey)
    @Query(value = "select p.* from product as p " +
            "left join product_brand_category as pbc " +
            "on p.id = pbc.product_id " +
            "where pbc.brand_id = :brandId " +
            "order by p.price asc limit :size", nativeQuery = true)
    List<Product> findBrandProductsByBrandIdNoKeyOrderByPriceAsc(@Param("brandId") Long brandId, @Param("size") int size);

    // 4. 가격 높은순(HasKey)
    @Query(value = "select p.* from product as p " +
            "left join product_brand_category as pbc " +
            "on p.id = pbc.product_id " +
            "where pbc.brand_id = :brandId and p.price < :price " +
            "order by p.price desc limit :size", nativeQuery = true)
    List<Product> findBrandProductsByBrandIdHasKeyOrderByPriceDesc(@Param("price") Integer price,
                                                                           @Param("brandId") Long brandId,
                                                                           @Param("size") int size);
    //    가격 높은순(NoKey)
    @Query(value = "select p.* from product as p " +
            "left join product_brand_category as pbc " +
            "on p.id = pbc.product_id " +
            "where pbc.brand_id = :brandId " +
            "order by p.price desc limit :size", nativeQuery = true)
    List<Product> findBrandProductsByBrandIdNoKeyOrderByPriceDesc(@Param("brandId") Long brandId,
                                                                             @Param("size") int size);

    // 카테고리 id 존재
    // 1. 최신순(HasKey)
    @Query(value = "select p.* from product as p " +
            "left join product_brand_category as pbc " +
            "on p.id = pbc.product_id " +
            "where pbc.brand_id = :brandId and pbc.brand_category_id = :categoryId and p.id < :id " +
            "order by p.id desc limit :size", nativeQuery = true)
    List<Product> findBrandProductsByBrandIdAndCategoryIdHasKey(@Param("id") Long id,
                                                                @Param("categoryId") Long categoryId,
                                                                @Param("brandId") Long brandId,
                                                                @Param("size") int size);
    //    최신순(NoKey)
    @Query(value = "select p.* from product as p " +
            "left join product_brand_category as pbc " +
            "on p.id = pbc.product_id " +
            "where pbc.brand_id = :brandId and pbc.brand_category_id = :categoryId " +
            "order by p.id desc limit :size", nativeQuery = true)
    List<Product> findBrandProductsByBrandIdAndCategoryIdNoKey(
                                                                  @Param("categoryId") Long categoryId,
                                                                  @Param("brandId") Long brandId,
                                                                  @Param("size") int size);

    // 2. 인기순(HasKey)
    @Query(value = "select p.* from product as p " +
            "left join product_brand_category as pbc on p.id = pbc.product_id " +
            "left join best as b on p.id = b.product_id " +
            "where pbc.brand_id = :brandId and pbc.brand_category_id = :categoryId and b.score < :score " +
            "order by b.score desc limit :size", nativeQuery = true)
    List<Product> findBrandProductsByBrandIdAndCategoryIdHasKeyOrderByScore(@Param("score") Double score,
                                                                       @Param("categoryId") Long categoryId,
                                                                       @Param("brandId") Long brandId,
                                                                       @Param("size") int size);
    //    인기순(NoKey)
    @Query(value = "select p.* from product as p " +
            "left join product_brand_category as pbc on p.id = pbc.product_id " +
            "left join best as b on p.id = b.product_id " +
            "where pbc.brand_id = :brandId and pbc.brand_category_id = :categoryId " +
            "order by b.score desc limit :size", nativeQuery = true)
    List<Product> findBrandProductsByBrandIdAndCategoryIdNoKeyOrderByScore(@Param("categoryId") Long categoryId,
                                                                       @Param("brandId") Long brandId,
                                                                       @Param("size") int size);

    // 3. 가격 낮은순(HasKey)
    @Query(value = "select p.* from product as p " +
            "left join product_brand_category as pbc " +
            "on p.id = pbc.product_id " +
            "where pbc.brand_id = :brandId and pbc.brand_category_id = :categoryId and p.price < :price " +
            "order by p.price asc limit :size", nativeQuery = true)
    List<Product> findBrandProductsByBrandIdAndCategoryIdHasKeyOrderByPriceAsc(@Param("price") Integer price,
                                                                        @Param("categoryId") Long categoryId,
                                                                        @Param("brandId") Long brandId,
                                                                        @Param("size") int size);
    //    가격 낮은순(NoKey)
    @Query(value = "select p.* from product as p " +
            "left join product_brand_category as pbc " +
            "on p.id = pbc.product_id " +
            "where pbc.brand_id = :brandId and pbc.brand_category_id = :categoryId " +
            "order by p.price asc limit :size", nativeQuery = true)
    List<Product> findBrandProductsByBrandIdAndCategoryIdNoKeyOrderByPriceAsc(@Param("categoryId") Long categoryId,
                                                                              @Param("brandId") Long brandId,
                                                                              @Param("size") int size);

    // 4. 가격 높은순(HasKey)
    @Query(value = "select p.* from product as p " +
            "left join product_brand_category as pbc " +
            "on p.id = pbc.product_id " +
            "where pbc.brand_id = :brandId and pbc.brand_category_id = :categoryId and p.price < :price " +
            "order by p.price desc limit :size", nativeQuery = true)
    List<Product> findBrandProductsByBrandIdAndCategoryIdHasKeyOrderByPriceDesc(@Param("price") Integer price,
                                                                          @Param("categoryId") Long categoryId,
                                                                          @Param("brandId") Long brandId,
                                                                          @Param("size") int size);
    //    가격 높은순(NoKey)
    @Query(value = "select p.* from product as p " +
            "left join product_brand_category as pbc " +
            "on p.id = pbc.product_id " +
            "where pbc.brand_id = :brandId and pbc.brand_category_id = :categoryId " +
            "order by p.price desc limit :size", nativeQuery = true)
    List<Product> findBrandProductsByBrandIdAndCategoryIdNoKeyOrderByPriceDesc(
            @Param("categoryId") Long categoryId,
            @Param("brandId") Long brandId,
            @Param("size") int size);

    //==============================================================================
    @Query(value = "select pbc.brand_category_id from product as p " +
            "left join product_brand_category as pbc " +
            "on p.id = pbc.product_id " +
            "where pbc.product_id = :productId", nativeQuery = true)
    Long findBrandCategoryId(Long productId);

    @Query(value = "select b.score from product as p " +
            "left join best as b on p.id = b.product_id " +
            "where b.product_id = :productId", nativeQuery = true)
    Double findProductScore(@Param("productId") Long productId);

}
