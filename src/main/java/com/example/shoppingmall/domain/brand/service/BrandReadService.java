package com.example.shoppingmall.domain.brand.service;

import com.example.shoppingmall.domain.brand.dto.res.BrandDetailResponse;
import com.example.shoppingmall.domain.brand.dto.res.BrandResponse;
import com.example.shoppingmall.domain.brand.entity.Brand;
import com.example.shoppingmall.domain.brand.repository.BrandRepository;
import com.example.shoppingmall.domain.brand.util.BrandInfoMapping;
import com.example.shoppingmall.domain.product.product.service.ProductUtilService;
import com.example.shoppingmall.domain.user.entity.User;
import com.example.shoppingmall.domain.user.service.UserReadService;
import com.example.shoppingmall.global.error.exception.ErrorCode;
import com.example.shoppingmall.global.error.exception.InvalidValueException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class BrandReadService {
    private final BrandRepository brandRepository;

    private final BrandLikeReadService brandLikeReadService;
    private final UserReadService userReadService;
    private final ProductUtilService productUtilService;


    public BrandDetailResponse getBrandDetail(Long brandId) {
        Brand brand = brandRepository.findBrandById(brandId);

        return toBrandDetailResponse(brand);
    }

    public List<BrandResponse> getAllBrand(Long sortId) {
        List<Brand> brandList = findAllBrand(sortId);

        return brandList.stream()
                .map(this::toBrandResponse)
                .collect(Collectors.toList());
    }

    public List<BrandResponse> getLikeBrands(User user, Long sortId) {
        List<Brand> likeBrandList = findAllLikeBrand(user, sortId);

        return likeBrandList.stream()
                .map(this::toBrandResponse)
                .collect(Collectors.toList());
    }

    public BrandInfoMapping getBrandInfo(Long productId) {
        return brandRepository.findBrandInfoByProductId(productId);
    }

    public void validatePrincipalLike(Principal principal, BrandDetailResponse brandDetailResponse) {
        if (principal != null) {
            Optional<User> userOptional = userReadService.getUserPrincipal(principal.getName());
            if (userOptional.isPresent()) {
                User user = userOptional.get();
                updateLikeTrue(user, brandDetailResponse);
            }
        }
    }

    private void updateLikeTrue(User user, BrandDetailResponse brandDetailResponse) {
        if (brandLikeReadService.isBrandLiked(user.getId(), brandDetailResponse.getId())) {
            brandDetailResponse.setLiked();
        }
    }

    private List<Brand> findAllBrand(Long sortId) {
        if (sortId.equals(0L)) {
            return brandRepository.findAllBrandsOrderByScoreDesc();
        } else if (sortId.equals(1L)) {
            return brandRepository.findAllBrandsOrderByNameAsc();
        } else if (sortId.equals(2L)) {
            return brandRepository.findAllBrandsOrderByNameDesc();
        } else {
            throw new InvalidValueException("Wrong SortId", ErrorCode.INVALID_INPUT_VALUE);
        }
    }

    private List<Brand> findAllLikeBrand(User user, Long sortId) {
        List<Brand> likedBrandList = new ArrayList<>();

        if (sortId.equals(0L)) {
             var findBrand = brandRepository.findLikeBrandsOrderByLike(user.getId());
             if (findBrand.isPresent()) {
                 return findBrand.get();
             }
        } else if (sortId.equals(1L)) {
            var findBrand = brandRepository.findLikeBrandsOrderByNameAsc(user.getId());

        } else if (sortId.equals(2L)) {
            return brandRepository.findLikeBrandsOrderByNameDesc(user.getId());
        } else {
            throw new InvalidValueException("Wrong SortId", ErrorCode.INVALID_INPUT_VALUE);
        }

        return likedBrandList;
    }


    private BrandResponse toBrandResponse(Brand brand) {
        return new BrandResponse(
                brand.getId(),
                brand.getName(),
                brand.getImageUrl()
        );
    }

    private BrandDetailResponse toBrandDetailResponse(Brand brand) {
        return BrandDetailResponse.builder()
                .id(brand.getId())
                .name(brand.getName())
                .categoryIds(productUtilService.getCategoryIdsMapping(brand.getId()))
                .logoUrl(brand.getLogoUrl())
                .imageUrl(brand.getImageUrl())
                .isLiked(false)
                .build();
    }
}
