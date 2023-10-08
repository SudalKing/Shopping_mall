package com.example.shoppingmall.application.controller;

import com.example.shoppingmall.application.usecase.product.CreateProductUseCase;
import com.example.shoppingmall.application.usecase.product.DeleteProductUseCase;
import com.example.shoppingmall.domain.product.dto.ProductCommand;
import com.example.shoppingmall.domain.product.dto.ProductCommentCommand;
import com.example.shoppingmall.domain.product.dto.ProductCommentDto;
import com.example.shoppingmall.domain.product.dto.ProductDto;
import com.example.shoppingmall.domain.product.service.ProductCommentReadService;
import com.example.shoppingmall.domain.product.service.ProductCommentWriteService;
import com.example.shoppingmall.domain.product.service.ProductReadService;
import com.example.shoppingmall.domain.product.service.ProductWriteService;
import com.example.shoppingmall.util.CursorRequest;
import com.example.shoppingmall.util.PageCursor;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Api(tags = "상품과 댓글 기능")
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


    @Operation(summary = "상품 등록",
            description = "ProductCommand와 fileType(=image), multipartFiles(이미지들)을 받아 상품 생성",
            tags = {"ADMIN_ROLE"})
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "OK",
                    content = @Content(schema = @Schema(implementation = ProductDto.class))
            )
    })
    @PostMapping("/add")
    public ResponseEntity<Object> createProduct(
            ProductCommand productCommand,
            @RequestParam(value = "fileType") String fileType,
            @RequestPart(value = "files")List<MultipartFile> multipartFiles
    ){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(createProductUseCase.execute(productCommand, fileType, multipartFiles));
    }


    @Operation(summary = "상품 수정",
            description = "수정하려는 상품의 productId와 ProductCommand를 받아 상품 수정",
            tags = {"ADMIN_ROLE"})
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "OK",
                    content = @Content(schema = @Schema(implementation = ProductDto.class))
            )
    })
    @PutMapping("/{productId}")
    public ProductDto updateProduct(@PathVariable Long productId, ProductCommand productCommand){
        var product = productWriteService.updateProduct(productId, productCommand);
        return productReadService.toDto(product);
    }


    @Operation(summary = "상품 삭제", description = "상품의 productId를 받아 상품 삭제", tags = {"ADMIN_ROLE"})
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "OK")})
    @DeleteMapping("/{productId}")
    public void deleteProduct(@PathVariable Long productId){
        deleteProductUseCase.execute(productId);
    }


// =============================================상품 조회=====================================================

    @Operation(summary = "모든 상품 조회",
            description = "모든 상품 조회(무한 스크롤 방식)",
            tags = {"USER_ROLE"})
    @GetMapping("/all")
    public PageCursor<ProductDto> readAllProducts(CursorRequest cursorRequest, Long sortId){
        return productReadService.getProductsByCursor(cursorRequest, sortId);
    }

    @Operation(summary = "NEW 상품 조회",
            description = "NEW 상품 조회",
            tags = {"USER_ROLE"})
    @GetMapping("/new/all")
    public PageCursor<ProductDto> readNEWProducts(CursorRequest cursorRequest, Long sortId){
        return productReadService.getNewProducts(cursorRequest, sortId);
    }

    @Operation(summary = "Sale 상품 조회",
            description = "Sale 상품 조회",
            tags = {"USER_ROLE"})
    @GetMapping("/sale/all")
    public PageCursor<ProductDto> readSaleProducts(CursorRequest cursorRequest, Long sortId){
        return productReadService.getSaleProducts(cursorRequest, sortId);
    }

    @Operation(summary = "Best 상품 조회",
            description = "Best 상품 조회",
            tags = {"USER_ROLE"})
    @GetMapping("/best/all")
    public PageCursor<ProductDto> readBestProducts(CursorRequest cursorRequest, Long sortId){
        return productReadService.getBestProducts(cursorRequest);
    }

    @Operation(summary = "의류 상품 조회",
            description = "의류 상품 조회",
            tags = {"USER_ROLE"})
    @GetMapping("/clothes/all")
    public PageCursor<ProductDto> readClothesProducts(CursorRequest cursorRequest, Long categoryId, Long sortId){
        return productReadService.getClothesProducts(cursorRequest, categoryId, sortId);
    }

    @Operation(summary = "소품 상품 조회",
            description = "소품 상품 조회",
            tags = {"USER_ROLE"})
    @GetMapping("/prop/all")
    public PageCursor<ProductDto> readPropProducts(CursorRequest cursorRequest, Long categoryId, Long sortId){
        return productReadService.getPropProducts(cursorRequest, categoryId, sortId);
    }

    @Operation(summary = "잡화 상품 조회",
            description = "잡화 상품 조회",
            tags = {"USER_ROLE"})
    @GetMapping("/goods/all")
    public PageCursor<ProductDto> readGoodsProducts(CursorRequest cursorRequest, Long categoryId, Long sortId){
        return productReadService.getGoodsProducts(cursorRequest, categoryId, sortId);
    }

    @Operation(summary = "홈리빙 상품 조회",
            description = "홈리빙 상품 조회",
            tags = {"USER_ROLE"})
    @GetMapping("/home-living/all")
    public PageCursor<ProductDto> readHomeLivingProducts(CursorRequest cursorRequest, Long categoryId, Long sortId){
        return productReadService.getHomeLivingProducts(cursorRequest, categoryId, sortId);
    }

    @Operation(summary = "뷰티 상품 조회",
            description = "뷰티 상품 조회",
            tags = {"USER_ROLE"})
    @GetMapping("/beauty/all")
    public PageCursor<ProductDto> readBeautyProducts(CursorRequest cursorRequest, Long categoryId, Long sortId){
        return productReadService.getBeautyProducts(cursorRequest, categoryId, sortId);
    }

    @Operation(summary = "브랜드별 상품 조회",
            description = "브랜드별 상품 조회",
            tags = {"USER_ROLE"})
    @GetMapping("/brand/all")
    public PageCursor<ProductDto> readBrandProducts(CursorRequest cursorRequest, Long categoryId, Long sortId){
        return productReadService.getBrandProducts(cursorRequest, categoryId, sortId);
    }
// ==================================================================================================================
// ====================================================== 상품 댓글 =====================================================
    @Operation(summary = "상품 댓글 생성",
            description = "사용자의 userId와 ProductCommentCommand를 받아 제품 댓글 생성",
            tags = {"USER_ROLE"})
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "OK",
                    content = @Content(schema = @Schema(implementation = ProductCommentDto.class))
            )
    })
    @PostMapping("/{userId}/comment")
    public ProductCommentDto createComment(@PathVariable Long userId, ProductCommentCommand productCommentCommand){
        var comment = productCommentWriteService.createProductComment(userId, productCommentCommand);
        return productCommentReadService.toDto(comment);
    }


    @Operation(summary = "상품의 모든 댓글 조회", description = "productId를 받아 상품의 모든 댓글을 읽어 List형 반환", tags = {"USER_ROLE"})
    @ApiResponse(
            responseCode = "200",
            description = "OK",
            content = @Content(
                    array = @ArraySchema(
                            schema = @Schema(implementation = ProductCommentDto.class)
                    )
            )
    )
    @GetMapping("/{productId}/comments")
    public List<ProductCommentDto> readAllCommentsByProductId(@PathVariable Long productId){
        return productCommentReadService.getAllComments(productId);
    }


    @Operation(summary = "상품 댓글 삭제", description = "userId와 commentId를 받아 댓글을 작성한 사용자가 맞다면 댓글 삭제", tags = {"USER_ROLE"})
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "OK")})
    @DeleteMapping("/{commentId}/comments")
    public void deleteComment(@PathVariable Long commentId, @RequestParam Long userId){
        productCommentWriteService.deleteProductComment(commentId, userId);
    }
//
//    @PostMapping("/brand")
//    public BrandCategory createBrand(String brandName){
//        return productWriteService.registerBrand(brandName);
//    }
}
