package com.example.shoppingmall.domain.brand.repository;

import com.example.shoppingmall.domain.brand.entity.BrandLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface BrandLikeRepository extends JpaRepository<BrandLike, Long> {
    Optional<BrandLike> findByUserIdAndBrandId(Long userId, Long brandId);
    void deleteAllByUserId(Long userId);
}
