package com.example.shoppingmall.domain.brand.service;

import com.example.shoppingmall.domain.brand.entity.Brand;
import com.example.shoppingmall.domain.brand.entity.BrandLike;
import com.example.shoppingmall.domain.brand.repository.BrandLikeRepository;
import com.example.shoppingmall.domain.brand.repository.BrandRepository;
import com.example.shoppingmall.domain.brand.dto.req.BrandCommand;
import com.example.shoppingmall.domain.user.entity.User;
import com.example.shoppingmall.domain.user.service.UserReadService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class BrandWriteService {
    private final BrandRepository brandRepository;
    private final BrandLikeRepository brandLikeRepository;

    private final UserReadService userReadService;

    public Brand createBrand(BrandCommand brandCommand){
        Brand brand = Brand.builder()
                .name(brandCommand.getName())
                .pathName(brandCommand.getPathName())
                .imageUrl(brandCommand.getImageUrl())
                .build();
        return brandRepository.save(brand);
    }

    public void createOrDeleteBrandLike(User user, Long brandId) {
        Optional<BrandLike> findBrandLike = brandLikeRepository.findByUserIdAndBrandId(user.getId(), brandId);

        if (findBrandLike.isEmpty()) {
            BrandLike brandLike = BrandLike.builder()
                    .userId(user.getId())
                    .brandId(brandId)
                    .build();
            brandLikeRepository.save(brandLike);
        } else {
            brandLikeRepository.delete(findBrandLike.get());
        }
    }
}
