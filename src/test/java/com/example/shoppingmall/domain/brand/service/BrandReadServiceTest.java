package com.example.shoppingmall.domain.brand.service;

import com.example.shoppingmall.domain.brand.dto.res.BrandDetailResponse;
import com.example.shoppingmall.domain.brand.repository.BrandRepository;
import com.example.shoppingmall.domain.brand.util.BrandInfoMapping;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class BrandReadServiceTest {
    @Autowired
    private BrandReadService brandReadService;

    @Autowired
    private BrandRepository brandRepository;

    private static final Long BRAND_ID = 1L;

    @DisplayName("1. [브랜드 상세 조회 테스트]")
    @Test
    void getBrandDetail() {
       BrandDetailResponse brandDetail = brandReadService.getBrandDetail(BRAND_ID);
        System.out.println(brandDetail);
    }

    @DisplayName("2. [전체 브랜드 조회 테스트]")
    @Test
    void getAllBrand() throws Exception {
       var brandResponseList = brandReadService.getAllBrand(0L);
       brandResponseList.forEach(
               brandResponse -> {
                   System.out.println(brandResponse.toString());
               }
       );
    }

    @DisplayName("3. [BrandInfo 조회]")
    @Test
    void getBrandInfo(){
        BrandInfoMapping brandInfo = brandRepository.findBrandInfoByProductId(1L);
        System.out.println("brandId: " + brandInfo.getId());
        System.out.println("brandName: " + brandInfo.getName());
    }
}
