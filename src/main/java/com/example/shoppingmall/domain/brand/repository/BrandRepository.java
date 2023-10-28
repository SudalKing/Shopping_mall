package com.example.shoppingmall.domain.brand.repository;

import com.example.shoppingmall.domain.brand.entity.Brand;
import com.example.shoppingmall.domain.brand.util.BrandInfoMapping;
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

    @Query(value = "select b.* from brand as b " +
            "where b.id in (select bl.brand_id " +
            "from brand_like as bl " +
            "where bl.user_id = :userId)", nativeQuery = true)
    List<Brand> findAllLikeBrandIds(@Param("userId") Long userId);

    @Query(value = "select b.id, b.name from brand as b " +
            "left join product_brand_category as pbc on b.id = pbc.brand_id " +
            "left join product as p on pbc.product_id = p.id " +
            "where p.id = :productId", nativeQuery = true)
    BrandInfoMapping findBrandInfoByProductId(@Param("productId") Long productId);
}
