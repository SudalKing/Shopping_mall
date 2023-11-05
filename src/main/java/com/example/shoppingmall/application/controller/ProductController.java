package com.example.shoppingmall.application.controller;

import com.example.shoppingmall.application.usecase.product.CreateProductUseCase;
import com.example.shoppingmall.application.usecase.product.DeleteProductUseCase;
import com.example.shoppingmall.domain.product.product.dto.ProductResponse;
import com.example.shoppingmall.domain.product.product.dto.req.ProductCommand;
import com.example.shoppingmall.domain.product.product.dto.res.ProductReadResponse;
import com.example.shoppingmall.domain.product.product.service.ProductLikeWriteService;
import com.example.shoppingmall.domain.product.product.service.ProductReadService;
import com.example.shoppingmall.domain.user.entity.User;
import com.example.shoppingmall.domain.user.service.UserReadService;
import com.example.shoppingmall.util.PageCursor;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.List;

@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping("/product")
public class ProductController {
    private final ProductReadService productReadService;
    private final ProductLikeWriteService productLikeWriteService;
    private final CreateProductUseCase createProductUseCase;
    private final DeleteProductUseCase deleteProductUseCase;
    private final UserReadService userReadService;


    @Operation(summary = "ProductCommand, multipartFiles(이미지들)을 받아 상품 생성", description = "[인증 필요(ADMIN)]")
    @ApiResponse(responseCode = "201", description = "OK")
    @PostMapping("/add")
    public ResponseEntity<Object> createProduct(
            @RequestPart ProductCommand productCommand,
            @RequestPart(value = "files")List<MultipartFile> multipartFiles
    ){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(createProductUseCase.execute(productCommand, "image", multipartFiles));
    }

    @Operation(summary = "상품 삭제", description = "[인증 필요(ADMIN)]")
    @ApiResponse(responseCode = "200", description = "OK")
    @DeleteMapping("/{productId}")
    public void deleteProduct(@PathVariable Long productId){
        deleteProductUseCase.execute(productId);
    }


// =============================================모든 상품 조회=====================================================
    @Operation(summary = "모든 상품 조회 sortId: 0(최신순), 1(인기순), 2(가격 낮은순), 3(가격 높은순)", description = "[인증 불필요]")
    @GetMapping("/get/all")
    public PageCursor<ProductResponse> readAllProductsDefault(Principal principal,
                                                              @RequestParam(required = false) Number key,
                                                              int size, Long sortId) throws Exception {
        var products = productReadService.getProductsByCursor(key, size, sortId);
        productReadService.validatePrincipalLike(principal, products.getBody());

        return products;
    }
// ================================================New 조회==================================================
    @Operation(summary = "NEW 상품 조회", description = "[인증 불필요]")
    @GetMapping("/get/new")
    public PageCursor<ProductResponse> readNEWProducts(Principal principal,
                                                       @RequestParam(required = false) Number key,
                                                       int size, Long sortId) throws Exception {
        var products = productReadService.getNewProductsByCursor(key, size, sortId);
        productReadService.validatePrincipalLike(principal, products.getBody());

        return products;
    }

//    // ===================================== Best ======================================
//    @Operation(summary = "Best 전체 상품 조회", description = "[인증 불필요]")
//    @GetMapping("/get/best/all")
//    public List<ProductResponse> readAllBestProducts(Principal principal) {
//        var products = productReadService.getAllBestProducts();
//        productReadService.validatePrincipalLike(principal, products);
//
//        return products;
//    }
//
//    @Operation(summary = "Best 상품 조회 - 상품 3개", description = "[인증 불필요]")
//    @GetMapping("/get/best")
//    public List<ProductResponse> readBestProducts(Principal principal, Long typeId, Long categoryId) {
//        var products = productReadService.getBestProducts(typeId, categoryId);
//        productReadService.validatePrincipalLike(principal, products);
//
//        return products;
//    }
//    // ===================================== Sale ======================================
    @Operation(summary = "Sale 상품 조회", description = "[인증 불필요]")
    @GetMapping("/get/sale")
    public PageCursor<ProductResponse> readSaleProducts(Principal principal,
                                                        @RequestParam(required = false) Number key,
                                                        int size, Long sortId) throws Exception {
        var products = productReadService.getSaleProductsByCursor(key, size, sortId);
        productReadService.validatePrincipalLike(principal, products.getBody());

        return products;
    }

//    // ============================================ 카테고리 상품 =========================================================
    @Operation(summary = "카테고리 상품 조회: subCategoryId = 0, 한 카테고리 전체 조회", description = "[인증 불필요]")
    @GetMapping("/get/category")
    public PageCursor<ProductResponse> readClothesProducts(Principal principal,
                                                           @RequestParam(required = false) Number key,
                                                           int size, Long sortId, Long categoryId, Long subCategoryId) throws Exception {
        var products = productReadService.
                getProductsByCursorByCategoryAndSubCategoryId(key, size, sortId, categoryId, subCategoryId);
        productReadService.validatePrincipalLike(principal, products.getBody());

        return products;
    }
//// =====================================================================================================================
//// =================================================== 브랜드 상품 조회 ===================================================
    @Operation(summary = "브랜드별 상품 조회", description = "[인증 불필요]")
    @GetMapping("/get/brand")
    public PageCursor<ProductResponse> readBrandProducts(Principal principal,
                                                         @RequestParam(required = false) Number key,
                                                         int size, Long sortId, Long brandId) throws Exception {
        var products = productReadService.getBrandProductsByCursor(key, size, sortId, brandId);
        productReadService.validatePrincipalLike(principal, products.getBody());

        return products;
    }

    @Operation(summary = "브랜드, 카테고리, 서브카테고리별 상품 조회", description = "[인증 불필요]")
    @GetMapping("/get/brand/category")
    public PageCursor<ProductResponse> readBrandProducts(Principal principal,
                                                         @RequestParam(required = false) Number key,
                                                         int size, Long sortId, Long brandId,
                                                         Long categoryId, Long subCategoryId) throws Exception {
        var products = productReadService.getBrandCategoryProductsByCursor(
                key, size, sortId, brandId, categoryId, subCategoryId);
        productReadService.validatePrincipalLike(principal, products.getBody());

        return products;
    }
    // ==================================================================================

    @Operation(summary = "상품 List 조회", description = "[인증 불필요]")
    @ApiResponse(responseCode = "200", description = "OK")
    @GetMapping("/get")
    public List<ProductReadResponse> readProducts(@RequestParam List<Long> productIds){
        return productReadService.getProductsByIds(productIds);
    }

    // =================================== Like ================================================
    @Operation(summary = "상품 좋아요", description = "[인증 필요]")
    @ApiResponse(responseCode = "200", description = "OK")
    @PostMapping("/like/{productId}")
    public void createOrDeleteProductLike(Principal principal, @PathVariable Long productId){
        User user = userReadService.getUserByEmail(principal.getName());
        productLikeWriteService.createOrDeleteProductLike(user, productId);
    }

}
