package com.example.shoppingmall.domain.brand.service;

import com.example.shoppingmall.domain.brand.entity.Brand;
import com.example.shoppingmall.domain.brand.repository.BrandRepository;
import com.example.shoppingmall.domain.brand.repository.ProductBrandCategoryRepository;
import com.example.shoppingmall.domain.brand.dto.res.BrandDetailResponse;
import com.example.shoppingmall.domain.brand.dto.res.BrandResponse;
import com.example.shoppingmall.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class BrandReadService {
    private final BrandRepository brandRepository;
    private final ProductBrandCategoryRepository productBrandCategoryRepository;

    public BrandDetailResponse getBrandDetail(Long brandId) {
        Brand brand = brandRepository.findBrandById(brandId);

        return new BrandDetailResponse(
                brand.getId(),
                brand.getName(),
                getBrandCategoryIds(brand),
                brand.getLogoUrl(),
                brand.getImageUrl()
        );
    }

    public List<BrandResponse> getAllBrand(Long sortId) throws Exception {
        List<Brand> brandList = findAllBrand(sortId);

        return brandList.stream()
                .map(this::toBrandResponse)
                .collect(Collectors.toList());
    }

    public List<BrandResponse> getLikeBrands(User user) {
        List<Brand> likeBrandList = brandRepository.findAllLikeBrandIds(user.getId());

        return likeBrandList.stream()
                .map(this::toBrandResponse)
                .collect(Collectors.toList());
    }

    private List<Brand> findAllBrand(Long sortId) throws Exception {
        if (sortId.equals(0L)) {
            return brandRepository.findAllBrandsOrderByScoreDesc();
        } else if (sortId.equals(1L)) {
            return brandRepository.findAllBrandsOrderByNameAsc();
        } else if (sortId.equals(2L)) {
            return brandRepository.findAllBrandsOrderByNameDesc();
        } else {
            throw new Exception("Wrong sortId!!!");
        }
    }

    private List<Long> getBrandCategoryIds(Brand brand) {
        return productBrandCategoryRepository.findCategoryIdGroupByBrandId(brand.getId());
    }

    private BrandResponse toBrandResponse(Brand brand) {
        return new BrandResponse(
                brand.getId(),
                brand.getName(),
                brand.getPathName(),
                brand.getImageUrl()
        );
    }

}
