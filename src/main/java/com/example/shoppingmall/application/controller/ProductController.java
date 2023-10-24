package com.example.shoppingmall.application.controller;

import com.example.shoppingmall.application.usecase.product.CreateProductUseCase;
import com.example.shoppingmall.application.usecase.product.DeleteProductUseCase;
import com.example.shoppingmall.domain.brand.dto.req.BrandProductRequest;
import com.example.shoppingmall.domain.product.dto.req.ProductCommand;
import com.example.shoppingmall.domain.product.dto.req.ProductCommentCommand;
import com.example.shoppingmall.domain.product.dto.ProductCommentDto;
import com.example.shoppingmall.domain.product.dto.ProductDto;
import com.example.shoppingmall.domain.product.dto.res.BrandProductResponse;
import com.example.shoppingmall.domain.product.service.ProductCommentReadService;
import com.example.shoppingmall.domain.product.service.ProductCommentWriteService;
import com.example.shoppingmall.domain.product.service.ProductReadService;
import com.example.shoppingmall.domain.product.service.ProductWriteService;
import com.example.shoppingmall.domain.user.entity.User;
import com.example.shoppingmall.domain.user.service.UserReadService;
import com.example.shoppingmall.util.CursorRequest;
import com.example.shoppingmall.util.PageCursor;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
    private final ProductWriteService productWriteService;
    private final ProductReadService productReadService;
    private final ProductCommentWriteService productCommentWriteService;
    private final ProductCommentReadService productCommentReadService;
    private final CreateProductUseCase createProductUseCase;
    private final DeleteProductUseCase deleteProductUseCase;
    private final UserReadService userReadService;


    @Operation(summary = "ProductCommand와 fileType(=image), multipartFiles(이미지들)을 받아 상품 생성", description = "[인증 필요(ADMIN)]")
    @ApiResponse(responseCode = "200", description = "OK")
    @PostMapping("/add")
    public ResponseEntity<Object> createProduct(
            ProductCommand productCommand,
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


// =============================================상품 조회=====================================================

    @Operation(summary = "모든 상품 조회", description = "[인증 불필요]")
    @GetMapping("/get/all")
    public PageCursor<ProductDto> readAllProducts(Long key, int size, Long sortId){
        CursorRequest cursorRequest = new CursorRequest(key, size);
        return productReadService.getProductsByCursor(cursorRequest, sortId);
    }

    @Operation(summary = "NEW 상품 조회", description = "[인증 불필요]")
    @GetMapping("/get/new")
    public PageCursor<ProductDto> readNEWProducts(Long key, int size, Long sortId){
        CursorRequest cursorRequest = new CursorRequest(key, size);
        return productReadService.getNewProducts(cursorRequest, sortId);
    }

    @Operation(summary = "Sale 상품 조회", description = "[인증 불필요]")
    @GetMapping("/get/sale")
    public PageCursor<ProductDto> readSaleProducts(Long key, int size, Long sortId){
        CursorRequest cursorRequest = new CursorRequest(key, size);
        return productReadService.getSaleProducts(cursorRequest, sortId);
    }

    @Operation(summary = "Best 상품 조회", description = "[인증 불필요]")
    @GetMapping("/get/best")
    public PageCursor<ProductDto> readBestProducts(Long key, int size, Long sortId){
        CursorRequest cursorRequest = new CursorRequest(key, size);
        return productReadService.getBestProducts(cursorRequest);
    }

    @Operation(summary = "의류 상품 조회", description = "[인증 불필요]")
    @GetMapping("/get/clothes")
    public PageCursor<ProductDto> readClothesProducts(Long key, int size, Long categoryId, Long sortId){
        CursorRequest cursorRequest = new CursorRequest(key, size);
        return productReadService.getClothesProducts(cursorRequest, categoryId, sortId);
    }

    @Operation(summary = "소품 상품 조회", description = "[인증 불필요]")
    @GetMapping("/get/prop")
    public PageCursor<ProductDto> readPropProducts(Long key, int size, Long categoryId, Long sortId){
        CursorRequest cursorRequest = new CursorRequest(key, size);
        return productReadService.getPropProducts(cursorRequest, categoryId, sortId);
    }

    @Operation(summary = "잡화 상품 조회", description = "[인증 불필요]")
    @GetMapping("/get/goods")
    public PageCursor<ProductDto> readGoodsProducts(Long key, int size, Long categoryId, Long sortId){
        CursorRequest cursorRequest = new CursorRequest(key, size);
        return productReadService.getGoodsProducts(cursorRequest, categoryId, sortId);
    }

    @Operation(summary = "홈리빙 상품 조회", description = "[인증 불필요]")
    @GetMapping("/get/home-living")
    public PageCursor<ProductDto> readHomeLivingProducts(Long key, int size, Long categoryId, Long sortId){
        CursorRequest cursorRequest = new CursorRequest(key, size);
        return productReadService.getHomeLivingProducts(cursorRequest, categoryId, sortId);
    }

    @Operation(summary = "뷰티 상품 조회", description = "[인증 불필요]")
    @GetMapping("/get/beauty")
    public PageCursor<ProductDto> readBeautyProducts(Long key, int size, Long categoryId, Long sortId){
        CursorRequest cursorRequest = new CursorRequest(key, size);
        return productReadService.getBeautyProducts(cursorRequest, categoryId, sortId);
    }

    @Operation(summary = "브랜드별 상품 조회", description = "[인증 불필요]")
    @GetMapping("/get/brand")
    public PageCursor<BrandProductResponse> readBrandProducts(Long key, int size, Long brandId, Long brandCategoryId, Long sortId) throws Exception {
        BrandProductRequest brandProductRequest = BrandProductRequest.builder()
                .cursorRequest(new CursorRequest(key, size))
                .brandId(brandId)
                .brandCategoryId(brandCategoryId)
                .sortId(sortId)
                .build();
        return productReadService.getBrandProducts(brandProductRequest);
    }
// ==================================================================================================================
// ====================================================== 상품 댓글 =====================================================
    @Operation(summary = "상품 댓글 생성", description = "[인증 필요]")
    @ApiResponse(responseCode = "200", description = "OK")
    @PostMapping("/comment/add")
    public ProductCommentDto createComment(Principal principal, ProductCommentCommand productCommentCommand){
        User user = userReadService.getUserByEmail(principal.getName());
        var comment = productCommentWriteService.createProductComment(user.getId(), productCommentCommand);
        return productCommentReadService.toDto(comment);
    }

    @Operation(summary = "상품의 모든 댓글 조회", description = "[인증 불필요]")
    @ApiResponse(responseCode = "200", description = "OK")
    @GetMapping("/{productId}/comments")
    public List<ProductCommentDto> readAllCommentsByProductId(@PathVariable Long productId){
        return productCommentReadService.getAllComments(productId);
    }

    @Operation(summary = "상품 댓글 삭제", description = "[인증 필요]")
    @ApiResponse(responseCode = "200", description = "OK")
    @DeleteMapping("/{commentId}/comments")
    public void deleteComment(@PathVariable Long commentId, @RequestParam Long userId){
        productCommentWriteService.deleteProductComment(commentId, userId);
    }

}
