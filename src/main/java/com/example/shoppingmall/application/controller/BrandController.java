package com.example.shoppingmall.application.controller;

import com.example.shoppingmall.domain.brand.dto.res.BrandDetailResponse;
import com.example.shoppingmall.domain.brand.dto.res.BrandResponse;
import com.example.shoppingmall.domain.brand.service.BrandReadService;
import com.example.shoppingmall.domain.brand.service.BrandWriteService;
import com.example.shoppingmall.domain.user.entity.User;
import com.example.shoppingmall.domain.user.service.UserReadService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/brand")
public class BrandController {
    private final BrandWriteService brandWriteService;
    private final BrandReadService brandReadService;
    private final UserReadService userReadService;

    @Operation(summary = "브랜드 전체 조회", description = "[인증 불필요]")
    @ApiResponse(responseCode = "200", description = "OK")
    @GetMapping("/get/all")
    public ResponseEntity<List<BrandResponse>> readBrandList(Long sortId) throws Exception {
        return ResponseEntity.ok(brandReadService.getAllBrand(sortId));
    }

    @Operation(summary = "브랜드 상세 조회", description = "[인증 불필요]")
    @ApiResponse(responseCode = "200", description = "OK")
    @GetMapping("/get/{brandId}")
    public ResponseEntity<BrandDetailResponse> readBrandDetail(Principal principal, @PathVariable Long brandId){
        BrandDetailResponse brandDetailResponse = brandReadService.getBrandDetail(brandId);
        brandReadService.validatePrincipalLike(principal, brandDetailResponse);

        return ResponseEntity.status(HttpStatus.OK).body(brandDetailResponse);
    }

    @Operation(summary = "좋아요 브랜드 조회 sortId: 0(좋아요순), 1(이름 오름차순), 2(이름 내림차순)", description = "[인증 필요]")
    @ApiResponse(responseCode = "200", description = "OK")
    @GetMapping("/like/get")
    public ResponseEntity<List<BrandResponse>> readLikeBrands(Principal principal, Long sortId) throws Exception {
        User user = userReadService.getUserByEmail(principal.getName());
        return ResponseEntity.ok(brandReadService.getLikeBrands(user, sortId));
    }

    @Operation(summary = "브랜드 좋아요", description = "[인증 필요]")
    @ApiResponse(responseCode = "200", description = "OK")
    @PostMapping("/like/{brandId}")
    public ResponseEntity<Void> createOrDeleteBrandLike(Principal principal, @PathVariable Long brandId){
        User user = userReadService.getUserByEmail(principal.getName());
        brandWriteService.createOrDeleteBrandLike(user, brandId);

        return ResponseEntity.ok().build();
    }

}
