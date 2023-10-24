package com.example.shoppingmall.domain.brand.repository;

import com.example.shoppingmall.domain.brand.entity.ProductBrandCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductBrandCategoryRepository extends JpaRepository<ProductBrandCategory, Long> {
    @Query(value = "select brand_category_id from product_brand_category " +
                   "where brand_id = :brandId " +
                   "group by brand_id, brand_category_id",
            nativeQuery = true)
    List<Long> findCategoryIdGroupByBrandId(@Param("brandId") Long brandId);
}
