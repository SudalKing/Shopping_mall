package com.example.shoppingmall.domain.brand.repository;

import com.example.shoppingmall.domain.brand.entity.Brand;
import com.example.shoppingmall.domain.brand.util.BrandInfoMapping;
import com.example.shoppingmall.domain.brand.util.CategoryIdsMapping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BrandRepository extends JpaRepository<Brand, Long> {
    Brand findBrandById(Long id);

    @Query(value = "select * from brand as b " +
            "left join brand_like as bl on b.id = bl.brand_id " +
            "group by b.id " +
            "order by count(bl.id) desc", nativeQuery = true)
    List<Brand> findAllBrandsOrderByScoreDesc();

    @Query(value = "select * from brand order by name desc", nativeQuery = true)
    List<Brand> findAllBrandsOrderByNameDesc();

    @Query(value = "select * from brand order by name asc", nativeQuery = true)
    List<Brand> findAllBrandsOrderByNameAsc();
    // ========================================

    @Query(value = "select b.* from brand as b left join brand_like as bl on b.id = bl.brand_id " +
            "where bl.user_id = userId order by count(b.id) desc", nativeQuery = true)
    List<Brand> findLikeBrandsOrderByLike(@Param("userId") Long userId);

    @Query(value = "select b.* from brand as b left join brand_like as bl on b.id = bl.brand_id " +
            "where bl.user_id = userId order by b.name asc", nativeQuery = true)
    List<Brand> findLikeBrandsOrderByNameAsc(@Param("userId") Long userId);

    @Query(value = "select b.* from brand as b left join brand_like as bl on b.id = bl.brand_id " +
            "where bl.user_id = userId order by b.name desc", nativeQuery = true)
    List<Brand> findLikeBrandsOrderByNameDesc(@Param("userId") Long userId);

    // ==================================================================

    @Query(value = "select b.id, b.name from brand as b left join brand_product as bp on b.id = bp.brand_id " +
            "where bp.product_id = :productId", nativeQuery = true)
    BrandInfoMapping findBrandInfoByProductId(@Param("productId") Long productId);

//
//    @Query(value = "select p.category_id, p.sub_category_id from product as p left join brand_product as bp " +
//            "on p.id = bp.product_id where bp.brand_id = :brandId", nativeQuery = true)
//    List<CategoryIdsMapping> findCategoryAndSubCategoryIdsByBrandId(@Param("brandId") Long brandId);
}
