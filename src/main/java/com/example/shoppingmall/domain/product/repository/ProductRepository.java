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
    // 정렬 기준이 바뀌면 cursor 기반 방식도 바뀌어야 다음 키가 넘어온다? price desc -> where price < :price?
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
    @Query(value = "select * from product where id < :id and created_at >= :days " +
            "order by id desc limit :size",
            nativeQuery = true)
    List<Product> findAllProductsBetween7DaysByCursorHasKey(@Param("id") Long id, @Param("days")LocalDateTime days, @Param("size") int size);

    @Query(value = "select * from product where created_at >= :days " +
            "order by id desc limit :size", nativeQuery = true)
    List<Product> findAllProductsBetween7DaysByCursorNoKey(@Param("days") LocalDateTime days, @Param("size") int size);

    // 2. 인기순
    @Query(value = "select * from product as p " +
            "left join best as b on p.id = b.product_id " +
            "where p.id < :id and p.created_at >= :days " +
            "order by b.score desc limit :size", nativeQuery = true)
    List<Product> findAllProductsBetween7DaysByCursorHasKeyOrderByScore(@Param("id") Long id, @Param("days")LocalDateTime days, @Param("size") int size);

    @Query(value = "select * from product as p " +
            "left join best as b on p.id = b.product_id " +
            "where p.created_at >= :days " +
            "order by b.score desc limit :size", nativeQuery = true)
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

    // ======================= Clothes, Prop, Goods, HomeLivings, Beauty ======================================
    // categoryId = 0 일 때, 즉 탭별로 모든 상품 조회
//
//    @Query(value = "select * from product as p left join best as b on p.id = b.product_id " +
//            "where p.id < :id and p.created_at >= :days order by b.score desc limit :size",
//            nativeQuery = true)
    // 1. 최신순(기본)
    @Query(value = "select * from product as p " +
            "left join product_brand_category as pbc " +
            "on p.id = pbc.product_id " +
            "where p.id < :id and p.type_id = :typeId " +
            "order by p.id desc limit :size", nativeQuery = true)
    List<Product> findAllTypeProductsHasKey(@Param("id") Long id, @Param("typeId") Long typeId, @Param("size") int size);

    @Query(value = "select * from product as p " +
            "left join product_brand_category as pbc " +
            "on p.id = pbc.product_id " +
            "where p.type_id = :typeId " +
            "order by p.id desc limit :size", nativeQuery = true)
    List<Product> findAllTypeProductsNoKey(@Param("typeId") Long typeId, @Param("size") int size);


    // 탭별 카테고리 조회
    @Query(value = "select * from product as p " +
            "left join product_brand_category as pbc " +
            "on p.id = pbc.product_id " +
            "where p.id < :id and p.type_id = :typeId and pbc.brand_category_id = :categoryId " +
            "order by p.id desc limit :size", nativeQuery = true)
    List<Product> findTypeCategoryProductsHasKey(@Param("id") Long id,
                                            @Param("typeId") Long typeId,
                                            @Param("categoryId") Long categoryId,
                                            @Param("size") int size);

    @Query(value = "select * from product as p " +
            "left join product_brand_category as pbc " +
            "on p.id = pbc.product_id " +
            "where p.type_id = :typeId and pbc.brand_category_id = :categoryId " +
            "order by p.id desc limit :size", nativeQuery = true)
    List<Product> findTypeCategoryProductsNoKey(@Param("typeId") Long typeId,
                                           @Param("categoryId") Long categoryId,
                                           @Param("size") int size);

    // 2. 인기순
    @Query(value = "select * from product as p " +
            "left join best as b on p.id = b.product_id " +
            "where p.id < :id and p.type_id = :typeId order by b.score desc limit :size;", nativeQuery = true)
    List<Product> findAllTypeProductsHasKeyOrderByScore(@Param("id") Long id, @Param("typeId") Long typeId, @Param("size") int size);

    @Query(value = "select * from product as p " +
            "left join best as b on p.id = b.product_id " +
            "where p.type_id = :typeId order by b.score desc limit :size;", nativeQuery = true)
    List<Product> findAllTypeProductsNoKeyOrderByScore(@Param("typeId") Long typeId, @Param("size") int size);

    @Query(value = "select * from product as p " +
            "left join best as b on p.id = b.product_id " +
            "left join product_brand_category as pbc on p.id = pbc.product_id " +
            "where p.id < :id and p.type_id = :typeId and pbc.category_id = :categoryId " +
            "order by b.score desc limit :size", nativeQuery = true)
    List<Product> findTypeCategoryProductsHasKeyOrderByScore(@Param("id") Long id,
                                                             @Param("typeId") Long typeId,
                                                             @Param("categoryId") Long categoryId,
                                                             @Param("size") int size);

    @Query(value = "select * from product as p " +
            "left join best as b on p.id = b.product_id " +
            "left join product_brand_category as pbc on p.id = pbc.product_id " +
            "where p.type_id = :typeId and pbc.category_id = :categoryId " +
            "order by b.score desc limit :size", nativeQuery = true)
    List<Product> findTypeCategoryProductsNoKeyOrderByScore(@Param("typeId") Long typeId,
                                                             @Param("categoryId") Long categoryId,
                                                             @Param("size") int size);

    // 3. 낮은 가격순
    @Query(value = "select * from product " +
            "where id < :id and type_id = :typeId " +
            "order by price asc limit :size", nativeQuery = true)
    List<Product> findAllTypeProductsHasKeyOrderByPriceAsc(@Param("id") Long id,
                                                           @Param("typeId") Long typeId,
                                                           @Param("size") int size);

    @Query(value = "select * from product " +
            "where type_id = :typeId " +
            "order by price asc limit :size", nativeQuery = true)
    List<Product> findAllTypeProductsNoKeyOrderByPriceAsc(@Param("typeId") Long typeId, @Param("size") int size);


    // 탭별 카테고리 조회
    @Query(value = "select * from product as p " +
            "left join product_brand_category as pbc on p.id = pbc.product_id " +
            "where p.id < :id and p.type_id = :typeId and pbc.brand_category_id = :categoryId " +
            "order by p.price asc limit :size", nativeQuery = true)
    List<Product> findTypeCategoryProductsHasKeyOrderByPriceAsc(@Param("id") Long id,
                                                 @Param("typeId") Long typeId,
                                                 @Param("categoryId") Long categoryId,
                                                 @Param("size") int size);

    @Query(value = "select * from product as p " +
            "left join product_brand_category as pbc on p.id = pbc.product_id " +
            "where p.type_id = :typeId and pbc.brand_category_id = :categoryId " +
            "order by p.price asc limit :size", nativeQuery = true)
    List<Product> findTypeCategoryProductsNoKeyOrderByPriceAsc(@Param("typeId") Long typeId,
                                                @Param("categoryId") Long categoryId,
                                                @Param("size") int size);

    // 4. 높은 가격순
    @Query(value = "select * from product " +
            "where id < :id and type_id = :typeId " +
            "order by price desc limit :size", nativeQuery = true)
    List<Product> findAllTypeProductsHasKeyOrderByPriceDesc(@Param("id") Long id,
                                                            @Param("typeId") Long typeId,
                                                            @Param("size") int size);

    @Query(value = "select * from product " +
            "where type_id = :typeId " +
            "order by price desc limit :size", nativeQuery = true)
    List<Product> findAllTypeProductsNoKeyOrderByPriceDesc(@Param("typeId") Long typeId, @Param("size") int size);

    // 탭별 카테고리 조회
    @Query(value = "select * from product as p " +
            "left join product_brand_category as pbc " +
            "where p.id < :id and p.type_id = :typeId and pbc.brand_category_id = :categoryId " +
            "order by p.price desc limit :size", nativeQuery = true)
    List<Product> findTypeCategoryProductsHasKeyOrderByPriceDesc(@Param("id") Long id,
                                                 @Param("typeId") Long typeId,
                                                 @Param("categoryId") Long categoryId,
                                                 @Param("size") int size);

    @Query(value = "select * from product as p " +
            "left join product_brand_category as pbc " +
            "where p.type_id = :typeId and pbc.brand_category_id = :categoryId " +
            "order by p.price desc limit :size", nativeQuery = true)
    List<Product> findTypeCategoryProductsNoKeyOrderByPriceDesc(@Param("typeId") Long typeId,
                                                @Param("categoryId") Long categoryId,
                                                @Param("size") int size);
    // ==============================================================================================================
    // ============================== Brand 별 상품 조회 ===========================================================
    // 카테고리 id 없음
    // 1. 최신순(HasKey)
    @Query(value = "select p.* from product as p " +
            "left join product_brand_category as pbc " +
            "on p.id = pbc.product_id " +
            "where pbc.brand_id = :brandId and p.id < :id " +
            "order by p.id desc limit :size", nativeQuery = true)
    List<Product> findAllProductsByBrandIdHasKeyOrderByRecent(@Param("id") Long id,
                                                                        @Param("brandId") Long brandId,
                                                                        @Param("size") int size);
    //    최신순(NoKey)
    @Query(value = "select p.* from product as p " +
            "left join product_brand_category as pbc " +
            "on p.id = pbc.product_id " +
            "where pbc.brand_id = :brandId " +
            "order by p.id desc limit :size", nativeQuery = true)
    List<Product> findAllProductsByBrandIdNoKeyOrderByRecent(@Param("brandId") Long brandId,
                                                                       @Param("size") int size);

    // 2. 인기순(HasKey)
    @Query(value = "select p.* from product as p " +
            "left join product_brand_category as pbc on p.id = pbc.product_id " +
            "left join best as b on p.id = b.product_id " +
            "where pbc.brand_id = :brandId and p.id < :id " +
            "order by b.score desc limit :size", nativeQuery = true)
    List<Product> findAllProductsByBrandIdHasKeyOrderByScore(@Param("id") Long id,
                                                                       @Param("brandId") Long brandId,
                                                                       @Param("size") int size);
    //    인기순(NoKey)
    @Query(value = "select p.* from product as p " +
            "left join product_brand_category as pbc on p.id = pbc.product_id " +
            "left join best as b on p.id = b.product_id " +
            "where pbc.brand_id = :brandId " +
            "order by b.score desc limit :size", nativeQuery = true)
    List<Product> findAllProductsByBrandIdNoKeyOrderByScore(@Param("brandId") Long brandId,
                                                                      @Param("size") int size);

    // 3. 가격 낮은순(HasKey)
    @Query(value = "select p.* from product as p " +
            "left join product_brand_category as pbc " +
            "on p.id = pbc.product_id " +
            "where pbc.brand_id = :brandId and p.id < :id " +
            "order by p.price asc limit :size", nativeQuery = true)
    List<Product> findAllProductsByBrandIdHasKeyOrderByPriceAsc(@Param("id") Long id,
                                                                          @Param("brandId") Long brandId,
                                                                          @Param("size") int size);
    //    가격 낮은순(NoKey)
    @Query(value = "select p.* from product as p " +
            "left join product_brand_category as pbc " +
            "on p.id = pbc.product_id " +
            "where pbc.brand_id = :brandId " +
            "order by p.price asc limit :size", nativeQuery = true)
    List<Product> findAllProductsByBrandIdNoKeyOrderByPriceAsc(@Param("brandId") Long brandId,
                                                                            @Param("size") int size);

    // 4. 가격 높은순(HasKey)
    @Query(value = "select p.* from product as p " +
            "left join product_brand_category as pbc " +
            "on p.id = pbc.product_id " +
            "where pbc.brand_id = :brandId and p.id < :id " +
            "order by p.price desc limit :size", nativeQuery = true)
    List<Product> findAllProductsByBrandIdHasKeyOrderByPriceDesc(@Param("id") Long id,
                                                                           @Param("brandId") Long brandId,
                                                                           @Param("size") int size);
    //    가격 높은순(NoKey)
    @Query(value = "select p.* from product as p " +
            "left join product_brand_category as pbc " +
            "on p.id = pbc.product_id " +
            "where pbc.brand_id = :brandId " +
            "order by p.price desc limit :size", nativeQuery = true)
    List<Product> findAllProductsByBrandIdNoKeyOrderByPriceDesc(@Param("brandId") Long brandId,
                                                                             @Param("size") int size);

    // 카테고리 id 존재
    // 1. 최신순(HasKey)
    @Query(value = "select p.* from product as p " +
            "left join product_brand_category as pbc " +
            "on p.id = pbc.product_id " +
            "where pbc.brand_id = :brandId and pbc.brand_category_id = :categoryId and p.id < :id " +
            "order by p.id desc limit :size", nativeQuery = true)
    List<Product> findProductsByBrandIdHasKeyOrderByRecent(@Param("id") Long id,
                                                                  @Param("categoryId") Long categoryId,
                                                                  @Param("brandId") Long brandId,
                                                                  @Param("size") int size);
    //    최신순(NoKey)
    @Query(value = "select p.* from product as p " +
            "left join product_brand_category as pbc " +
            "on p.id = pbc.product_id " +
            "where pbc.brand_id = :brandId and pbc.brand_category_id = :categoryId " +
            "order by p.id desc limit :size", nativeQuery = true)
    List<Product> findProductsByBrandIdNoKeyOrderByRecent(
                                                                  @Param("categoryId") Long categoryId,
                                                                  @Param("brandId") Long brandId,
                                                                  @Param("size") int size);

    // 2. 인기순(HasKey)
    @Query(value = "select p.* from product as p " +
            "left join product_brand_category as pbc on p.id = pbc.product_id " +
            "left join best as b on p.id = b.product_id " +
            "where pbc.brand_id = :brandId and pbc.brand_category_id = :categoryId and p.id < :id " +
            "order by b.score desc limit :size", nativeQuery = true)
    List<Product> findProductsByBrandIdHasKeyOrderByScore(@Param("id") Long id,
                                                                       @Param("categoryId") Long categoryId,
                                                                       @Param("brandId") Long brandId,
                                                                       @Param("size") int size);
    //    인기순(NoKey)
    @Query(value = "select p.* from product as p " +
            "left join product_brand_category as pbc on p.id = pbc.product_id " +
            "left join best as b on p.id = b.product_id " +
            "where pbc.brand_id = :brandId and pbc.brand_category_id = :categoryId " +
            "order by b.score desc limit :size", nativeQuery = true)
    List<Product> findProductsByBrandIdNoKeyOrderByScore(@Param("categoryId") Long categoryId,
                                                                       @Param("brandId") Long brandId,
                                                                       @Param("size") int size);

    // 3. 가격 낮은순(HasKey)
    @Query(value = "select p.* from product as p " +
            "left join product_brand_category as pbc " +
            "on p.id = pbc.product_id " +
            "where pbc.brand_id = :brandId and pbc.brand_category_id = :categoryId and p.id < :id " +
            "order by p.price asc limit :size", nativeQuery = true)
    List<Product> findProductsByBrandIdHasKeyOrderByPriceAsc(@Param("id") Long id,
                                                                        @Param("categoryId") Long categoryId,
                                                                        @Param("brandId") Long brandId,
                                                                        @Param("size") int size);
    //    가격 낮은순(NoKey)
    @Query(value = "select p.* from product as p " +
            "left join product_brand_category as pbc " +
            "on p.id = pbc.product_id " +
            "where pbc.brand_id = :brandId and pbc.brand_category_id = :categoryId " +
            "order by p.price asc limit :size", nativeQuery = true)
    List<Product> findProductsByBrandIdNoKeyOrderByPriceAsc(@Param("categoryId") Long categoryId,
                                                            @Param("brandId") Long brandId,
                                                            @Param("size") int size);

    // 4. 가격 높은순(HasKey)
    @Query(value = "select p.* from product as p " +
            "left join product_brand_category as pbc " +
            "on p.id = pbc.product_id " +
            "where pbc.brand_id = :brandId and pbc.brand_category_id = :categoryId and p.id < :id " +
            "order by p.price desc limit :size", nativeQuery = true)
    List<Product> findProductsByBrandIdHasKeyOrderByPriceDesc(@Param("id") Long id,
                                                                          @Param("categoryId") Long categoryId,
                                                                          @Param("brandId") Long brandId,
                                                                          @Param("size") int size);
    //    가격 높은순(NoKey)
    @Query(value = "select p.* from product as p " +
            "left join product_brand_category as pbc " +
            "on p.id = pbc.product_id " +
            "where pbc.brand_id = :brandId and pbc.brand_category_id = :categoryId " +
            "order by p.price desc limit :size", nativeQuery = true)
    List<Product> findProductsByBrandIdNoKeyOrderByPriceDesc(
            @Param("categoryId") Long categoryId,
            @Param("brandId") Long brandId,
            @Param("size") int size);

    //==============================================================================
    @Query(value = "select pbc.brand_category_id from product as p " +
            "left join product_brand_category as pbc " +
            "on p.id = pbc.product_id " +
            "where pbc.product_id = :productId", nativeQuery = true)
    Long findBrandCategoryId(Long productId);
}
