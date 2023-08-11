package com.example.shoppingmall.domain.product.repository;

import com.example.shoppingmall.domain.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Product findProductById(Long id);

    @Query(value = "select * from product where id < :id order by id desc limit :size", nativeQuery = true)
    List<Product> findAllProductsByCursorHasKey(@Param("id") Long id, @Param("size") int size);

    @Query(value = "select * from product order by id desc limit :size", nativeQuery = true)
    List<Product> findAllProductsByCursorNoKey(@Param("size") int size);

}
