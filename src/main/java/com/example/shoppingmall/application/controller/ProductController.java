package com.example.shoppingmall.application.controller;

import com.example.shoppingmall.application.usecase.product.CreateProductUseCase;
import com.example.shoppingmall.application.usecase.product.DeleteProductUseCase;
import com.example.shoppingmall.domain.product.dto.ProductDto;
import com.example.shoppingmall.domain.product.dto.ProductReviewDto;
import com.example.shoppingmall.domain.product.dto.req.ProductCommand;
import com.example.shoppingmall.domain.product.dto.req.ProductReviewRequest;
import com.example.shoppingmall.domain.product.dto.res.ProductOrderResponse;
import com.example.shoppingmall.domain.product.service.*;
import com.example.shoppingmall.domain.user.entity.User;
import com.example.shoppingmall.domain.user.service.UserReadService;
import com.example.shoppingmall.util.CursorRequest;
import com.example.shoppingmall.util.PageCursor;
import com.example.shoppingmall.util.product.pagination.PriceCursorRequest;
import com.example.shoppingmall.util.product.pagination.PricePageCursor;
import com.example.shoppingmall.util.product.pagination.ScoreCursorRequest;
import com.example.shoppingmall.util.product.pagination.ScorePageCursor;
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
    private final ProductReviewWriteService productReviewWriteService;
    private final ProductReviewReadService productReviewReadService;
    private final ProductLikeWriteService productLikeWriteService;
    private final ProductReviewLikeWriteService productReviewLikeWriteService;
    private final CreateProductUseCase createProductUseCase;
    private final DeleteProductUseCase deleteProductUseCase;
    private final UserReadService userReadService;


    private final static String ALL = "all";
    private final static String NEW = "new";
    private final static String BEST = "best";
    private final static String SALE = "sale";
    private final static String CLOTHES = "clothes";
    private final static String PROP = "prop";
    private final static String GOODS = "goods";
    private final static String HOMELIVINGS = "homeLivings";
    private final static String BEAUTY = "beauty";
    private final static String BRAND = "brand";

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


// =============================================모든 상품 조회=====================================================
    @Operation(summary = "모든 상품 조회(최신순)", description = "[인증 불필요]")
    @GetMapping("/get/all/0")
    public PageCursor<ProductDto> readAllProductsDefault(Principal principal, Long key, int size) throws Exception {
        CursorRequest cursorRequest = new CursorRequest(key, size);
        var products = productReadService.getProductsByCursor(cursorRequest, ALL);
        productReadService.validatePrincipalLike(principal, products.getBody());

        return products;
    }

    @Operation(summary = "모든 상품 조회(인기순)", description = "[인증 불필요]")
    @GetMapping("/get/all/1")
    public ScorePageCursor<ProductDto> readAllProductsScore(Principal principal, Double key, int size) throws Exception {
        ScoreCursorRequest scoreCursorRequest = new ScoreCursorRequest(key, size);
        var products = productReadService.getProductsByCursorOrderByScore(scoreCursorRequest, ALL);
        productReadService.validatePrincipalLike(principal, products.getBody());

        return products;
    }

    @Operation(summary = "모든 상품 조회(가격 낮은순)", description = "[인증 불필요]")
    @GetMapping("/get/all/2")
    public PricePageCursor<ProductDto> readAllProductsPriceAsc(Principal principal, Integer key, int size) throws Exception {
        PriceCursorRequest priceCursorRequest = new PriceCursorRequest(key, size);
        var products = productReadService.getProductsByCursorOrderByPriceAsc(priceCursorRequest, ALL);
        productReadService.validatePrincipalLike(principal, products.getBody());
        return products;
    }

    @Operation(summary = "모든 상품 조회(가격 높은순)", description = "[인증 불필요]")
    @GetMapping("/get/all/3")
    public PricePageCursor<ProductDto> readAllProductsPriceDesc(Principal principal, Integer key, int size) throws Exception {
        PriceCursorRequest priceCursorRequest = new PriceCursorRequest(key, size);
        var products = productReadService.getProductsByCursorOrderByPriceDesc(priceCursorRequest, ALL);
        productReadService.validatePrincipalLike(principal, products.getBody());
        return products;
    }
// ================================================New 조회==================================================
    @Operation(summary = "NEW 상품 조회", description = "[인증 불필요]")
    @GetMapping("/get/new/0")
    public PageCursor<ProductDto> readNEWProducts(Principal principal, Long key, int size) throws Exception {
        CursorRequest cursorRequest = new CursorRequest(key, size);
        var products = productReadService.getProductsByCursor(cursorRequest, NEW);
        productReadService.validatePrincipalLike(principal, products.getBody());

        return products;
    }

    @Operation(summary = "NEW 상품 조회(인기순)", description = "[인증 불필요]")
    @GetMapping("/get/new/1")
    public ScorePageCursor<ProductDto> readNEWProductsScore(Principal principal, Double key, int size) throws Exception {
        ScoreCursorRequest scoreCursorRequest = new ScoreCursorRequest(key, size);
        var products = productReadService.getProductsByCursorOrderByScore(scoreCursorRequest, NEW);
        productReadService.validatePrincipalLike(principal, products.getBody());

        return products;
    }

    @Operation(summary = "NEW 상품 조회(가격 낮은순)", description = "[인증 불필요]")
    @GetMapping("/get/new/2")
    public PricePageCursor<ProductDto> readNEWProductsPriceAsc(Principal principal, Integer key, int size) throws Exception {
        PriceCursorRequest priceCursorRequest = new PriceCursorRequest(key, size);
        var products = productReadService.getProductsByCursorOrderByPriceAsc(priceCursorRequest, NEW);
        productReadService.validatePrincipalLike(principal, products.getBody());

        return products;
    }

    @Operation(summary = "NEW 상품 조회(가격 높은순)", description = "[인증 불필요]")
    @GetMapping("/get/new/3")
    public PricePageCursor<ProductDto> readNEWProductsPriceDesc(Principal principal, Integer key, int size) throws Exception {
        PriceCursorRequest priceCursorRequest = new PriceCursorRequest(key, size);
        var products = productReadService.getProductsByCursorOrderByPriceDesc(priceCursorRequest, NEW);
        productReadService.validatePrincipalLike(principal, products.getBody());

        return products;
    }
    // ===================================== Best ======================================
    @Operation(summary = "Best 전체 상품 조회", description = "[인증 불필요]")
    @GetMapping("/get/best/all")
    public List<ProductDto> readAllBestProducts(Principal principal, Long typeId, Long categoryId) {
        var products = productReadService.getBestProducts(typeId, categoryId);
        productReadService.validatePrincipalLike(principal, products);

        return products;
    }

    @Operation(summary = "Best 상품 조회 - 상품 3개", description = "[인증 불필요]")
    @GetMapping("/get/best")
    public List<ProductDto> readBestProducts(Principal principal, Long typeId, Long categoryId) {
        var products = productReadService.getBestProducts(typeId, categoryId);
        productReadService.validatePrincipalLike(principal, products);

        return products;
    }
    // ===================================== Sale ======================================
    @Operation(summary = "Sale 상품 조회", description = "[인증 불필요]")
    @GetMapping("/get/sale/0")
    public PageCursor<ProductDto> readSaleProducts(Principal principal, Long key, int size) throws Exception {
        CursorRequest cursorRequest = new CursorRequest(key, size);
        var products = productReadService.getProductsByCursor(cursorRequest, SALE);
        productReadService.validatePrincipalLike(principal, products.getBody());

        return products;
    }

    @Operation(summary = "Sale 상품 조회(인기순)", description = "[인증 불필요]")
    @GetMapping("/get/sale/1")
    public ScorePageCursor<ProductDto> readSaleProductsScore(Principal principal, Double key, int size) throws Exception {
        ScoreCursorRequest scoreCursorRequest = new ScoreCursorRequest(key, size);
        var products = productReadService.getProductsByCursorOrderByScore(scoreCursorRequest, SALE);
        productReadService.validatePrincipalLike(principal, products.getBody());

        return products;
    }

    @Operation(summary = "Sale 상품 조회(가격 낮은순)", description = "[인증 불필요]")
    @GetMapping("/get/sale/2")
    public PricePageCursor<ProductDto> readSaleProductsPriceAsc(Principal principal, Integer key, int size) throws Exception {
        PriceCursorRequest priceCursorRequest = new PriceCursorRequest(key, size);
        var products = productReadService.getProductsByCursorOrderByPriceAsc(priceCursorRequest, SALE);
        productReadService.validatePrincipalLike(principal, products.getBody());

        return products;
    }

    @Operation(summary = "Sale 상품 조회(가격 높은순)", description = "[인증 불필요]")
    @GetMapping("/get/sale/3")
    public PricePageCursor<ProductDto> readSaleProductsPriceDesc(Principal principal, Integer key, int size) throws Exception {
        PriceCursorRequest priceCursorRequest = new PriceCursorRequest(key, size);
        var products = productReadService.getProductsByCursorOrderByPriceDesc(priceCursorRequest, SALE);
        productReadService.validatePrincipalLike(principal, products.getBody());

        return products;
    }

    // ======================================= 의류 typeId = 1 =========================================================
    @Operation(summary = "의류 상품 조회", description = "[인증 불필요]")
    @GetMapping("/get/clothes/0")
    public PageCursor<ProductDto> readClothesProducts(Principal principal, Long key, int size, Long categoryId) throws Exception {
        CursorRequest cursorRequest = new CursorRequest(key, size);
        var products = productReadService.getProductsByCursorHasCategory(cursorRequest, CLOTHES, categoryId);
        productReadService.validatePrincipalLike(principal, products.getBody());

        return products;
    }

    @Operation(summary = "의류 상품 조회(인기순)", description = "[인증 불필요]")
    @GetMapping("/get/clothes/1")
    public ScorePageCursor<ProductDto> readClothesProductsScore(Principal principal, Double key, int size, Long categoryId) throws Exception {
        ScoreCursorRequest scoreCursorRequest = new ScoreCursorRequest(key, size);
        var products = productReadService.getProductsByCursorHasCategoryOrderByScore(scoreCursorRequest, CLOTHES, categoryId);
        productReadService.validatePrincipalLike(principal, products.getBody());

        return products;
    }

    @Operation(summary = "의류 상품 조회(가격 낮은순)", description = "[인증 불필요]")
    @GetMapping("/get/clothes/2")
    public PricePageCursor<ProductDto> readClothesProductsPriceAsc(Principal principal, Integer key, int size, Long categoryId) throws Exception {
        PriceCursorRequest priceCursorRequest = new PriceCursorRequest(key, size);
        var products = productReadService.getProductsByCursorHasCategoryOrderByPriceAsc(priceCursorRequest, CLOTHES, categoryId);
        productReadService.validatePrincipalLike(principal, products.getBody());

        return products;
    }

    @Operation(summary = "의류 상품 조회(가격 높은순)", description = "[인증 불필요]")
    @GetMapping("/get/clothes/3")
    public PricePageCursor<ProductDto> readClothesProductsPriceDesc(Principal principal, Integer key, int size, Long categoryId) throws Exception {
        PriceCursorRequest priceCursorRequest = new PriceCursorRequest(key, size);
        var products = productReadService.getProductsByCursorHasCategoryOrderByPriceDesc(priceCursorRequest, CLOTHES, categoryId);
        productReadService.validatePrincipalLike(principal, products.getBody());

        return products;
    }
// =====================================================================================================================
// ============================================ 소품 typeId = 2 =========================================================
    @Operation(summary = "소품 상품 조회", description = "[인증 불필요]")
    @GetMapping("/get/prop/0")
    public PageCursor<ProductDto> readPropProducts(Principal principal, Long key, int size) throws Exception {
        CursorRequest cursorRequest = new CursorRequest(key, size);
        var products = productReadService.getProductsByCursor(cursorRequest, PROP);
        productReadService.validatePrincipalLike(principal, products.getBody());

        return products;
    }

    @Operation(summary = "소품 상품 조회(인기순)", description = "[인증 불필요]")
    @GetMapping("/get/prop/1")
    public ScorePageCursor<ProductDto> readPropProductsScore(Principal principal, Double key, int size) throws Exception {
        ScoreCursorRequest scoreCursorRequest = new ScoreCursorRequest(key, size);
        var products = productReadService.getProductsByCursorOrderByScore(scoreCursorRequest, PROP);
        productReadService.validatePrincipalLike(principal, products.getBody());

        return products;
    }

    @Operation(summary = "소품 상품 조회(가격 낮은순)", description = "[인증 불필요]")
    @GetMapping("/get/prop/2")
    public PricePageCursor<ProductDto> readPropProductsPriceAsc(Principal principal, Integer key, int size) throws Exception {
        PriceCursorRequest priceCursorRequest = new PriceCursorRequest(key, size);
        var products = productReadService.getProductsByCursorOrderByPriceAsc(priceCursorRequest, PROP);
        productReadService.validatePrincipalLike(principal, products.getBody());

        return products;
    }

    @Operation(summary = "소품 상품 조회(가격 높은순)", description = "[인증 불필요]")
    @GetMapping("/get/prop/3")
    public PricePageCursor<ProductDto> readPropProductsPriceDesc(Principal principal, Integer key, int size) throws Exception {
        PriceCursorRequest priceCursorRequest = new PriceCursorRequest(key, size);
        var products = productReadService.getProductsByCursorOrderByPriceDesc(priceCursorRequest, PROP);
        productReadService.validatePrincipalLike(principal, products.getBody());

        return products;
    }
// =====================================================================================================================
// ======================================================= 잡화 =========================================================
    @Operation(summary = "잡화 상품 조회", description = "[인증 불필요]")
    @GetMapping("/get/goods/0")
    public PageCursor<ProductDto> readGoodsProducts(Principal principal, Long key, int size) throws Exception {
        CursorRequest cursorRequest = new CursorRequest(key, size);
        var products = productReadService.getProductsByCursor(cursorRequest, GOODS);
        productReadService.validatePrincipalLike(principal, products.getBody());

        return products;
    }

    @Operation(summary = "잡화 상품 조회(인기순)", description = "[인증 불필요]")
    @GetMapping("/get/goods/1")
    public ScorePageCursor<ProductDto> readGoodsProductsScore(Principal principal, Double key, int size) throws Exception {
        ScoreCursorRequest scoreCursorRequest = new ScoreCursorRequest(key, size);
        var products = productReadService.getProductsByCursorOrderByScore(scoreCursorRequest, GOODS);
        productReadService.validatePrincipalLike(principal, products.getBody());

        return products;
    }

    @Operation(summary = "잡화 상품 조회(가격 낮은순)", description = "[인증 불필요]")
    @GetMapping("/get/goods/2")
    public PricePageCursor<ProductDto> readGoodsProductsPriceAsc(Principal principal, Integer key, int size) throws Exception {
        PriceCursorRequest priceCursorRequest = new PriceCursorRequest(key, size);
        var products = productReadService.getProductsByCursorOrderByPriceAsc(priceCursorRequest, GOODS);
        productReadService.validatePrincipalLike(principal, products.getBody());

        return products;
    }

    @Operation(summary = "잡화 상품 조회(가격 높은순)", description = "[인증 불필요]")
    @GetMapping("/get/goods/3")
    public PricePageCursor<ProductDto> readGoodsProductsPriceDesc(Principal principal, Integer key, int size) throws Exception {
        PriceCursorRequest priceCursorRequest = new PriceCursorRequest(key, size);
        var products = productReadService.getProductsByCursorOrderByPriceDesc(priceCursorRequest, GOODS);
        productReadService.validatePrincipalLike(principal, products.getBody());

        return products;
    }
// =====================================================================================================================
// ====================================================== 홈리빙 =========================================================
    @Operation(summary = "홈리빙 상품 조회", description = "[인증 불필요]")
    @GetMapping("/get/home-living/0")
    public PageCursor<ProductDto> readHomeLivingProducts(Principal principal, Long key, int size) throws Exception {
        CursorRequest cursorRequest = new CursorRequest(key, size);
        var products = productReadService.getProductsByCursor(cursorRequest, HOMELIVINGS);
        productReadService.validatePrincipalLike(principal, products.getBody());

        return products;
    }

    @Operation(summary = "홈리빙 상품 조회(인기순)", description = "[인증 불필요]")
    @GetMapping("/get/home-living/1")
    public ScorePageCursor<ProductDto> readHomeLivingProductsScore(Principal principal, Double key, int size) throws Exception {
        ScoreCursorRequest scoreCursorRequest = new ScoreCursorRequest(key, size);
        var products = productReadService.getProductsByCursorOrderByScore(scoreCursorRequest, HOMELIVINGS);
        productReadService.validatePrincipalLike(principal, products.getBody());

        return products;
    }

    @Operation(summary = "홈리빙 상품 조회(가격 낮은순)", description = "[인증 불필요]")
    @GetMapping("/get/home-living/2")
    public PricePageCursor<ProductDto> readHomeLivingProductsPriceAsc(Principal principal, Integer key, int size) throws Exception {
        PriceCursorRequest priceCursorRequest = new PriceCursorRequest(key, size);
        var products = productReadService.getProductsByCursorOrderByPriceAsc(priceCursorRequest, HOMELIVINGS);
        productReadService.validatePrincipalLike(principal, products.getBody());

        return products;
    }

    @Operation(summary = "홈리빙 상품 조회(가격 높은순)", description = "[인증 불필요]")
    @GetMapping("/get/home-living/3")
    public PricePageCursor<ProductDto> readHomeLivingProductsPriceDesc(Principal principal, Integer key, int size) throws Exception {
        PriceCursorRequest priceCursorRequest = new PriceCursorRequest(key, size);
        var products = productReadService.getProductsByCursorOrderByPriceDesc(priceCursorRequest, HOMELIVINGS);
        productReadService.validatePrincipalLike(principal, products.getBody());

        return products;
    }
// =====================================================================================================================
// ======================================================= 뷰티 =========================================================
    @Operation(summary = "뷰티 상품 조회", description = "[인증 불필요]")
    @GetMapping("/get/beauty/0")
    public PageCursor<ProductDto> readBeautyProducts(Principal principal, Long key, int size) throws Exception {
        CursorRequest cursorRequest = new CursorRequest(key, size);
        var products = productReadService.getProductsByCursor(cursorRequest, BEAUTY);
        productReadService.validatePrincipalLike(principal, products.getBody());

        return products;
    }

    @Operation(summary = "뷰티 상품 조회(인기순)", description = "[인증 불필요]")
    @GetMapping("/get/beauty/1")
    public ScorePageCursor<ProductDto> readBeautyProductsScore(Principal principal, Double key, int size) throws Exception {
        ScoreCursorRequest scoreCursorRequest = new ScoreCursorRequest(key, size);
        var products = productReadService.getProductsByCursorOrderByScore(scoreCursorRequest, BEAUTY);
        productReadService.validatePrincipalLike(principal, products.getBody());

        return products;
    }

    @Operation(summary = "뷰티 상품 조회(가격 낮은순)", description = "[인증 불필요]")
    @GetMapping("/get/beauty/2")
    public PricePageCursor<ProductDto> readBeautyProductsPriceAsc(Principal principal, Integer key, int size) throws Exception {
        PriceCursorRequest priceCursorRequest = new PriceCursorRequest(key, size);
        var products = productReadService.getProductsByCursorOrderByPriceAsc(priceCursorRequest, BEAUTY);
        productReadService.validatePrincipalLike(principal, products.getBody());

        return products;
    }

    @Operation(summary = "뷰티 상품 조회(가격 높은순)", description = "[인증 불필요]")
    @GetMapping("/get/beauty/3")
    public PricePageCursor<ProductDto> readBeautyProductsPriceDesc(Principal principal, Integer key, int size) throws Exception {
        PriceCursorRequest priceCursorRequest = new PriceCursorRequest(key, size);
        var products = productReadService.getProductsByCursorOrderByPriceDesc(priceCursorRequest, BEAUTY);
        productReadService.validatePrincipalLike(principal, products.getBody());

        return products;
    }
// =====================================================================================================================
// =================================================== 브랜드 상품 조회 ===================================================
    @Operation(summary = "브랜드 상품 조회", description = "[인증 불필요]")
    @GetMapping("/get/brand/0")
    public PageCursor<ProductDto> readBrandProducts(Principal principal, Long key, int size, Long brandId, Long categoryId) throws Exception {
        CursorRequest cursorRequest = new CursorRequest(key, size);
        var products = productReadService.getBrandProductsByCursor(cursorRequest, brandId, categoryId);
        productReadService.validatePrincipalLike(principal, products.getBody());

        return products;
    }

    @Operation(summary = "브랜드 상품 조회(인기순)", description = "[인증 불필요]")
    @GetMapping("/get/brand/1")
    public ScorePageCursor<ProductDto> readBrandProductsScore(Principal principal, Double key, int size, Long brandId, Long categoryId) throws Exception {
        ScoreCursorRequest scoreCursorRequest = new ScoreCursorRequest(key, size);
        var products = productReadService.getBrandProductsByCursorOrderByScore(scoreCursorRequest, brandId, categoryId);
        productReadService.validatePrincipalLike(principal, products.getBody());

        return products;
    }

    @Operation(summary = "브랜드 상품 조회(가격 낮은순)", description = "[인증 불필요]")
    @GetMapping("/get/brand/2")
    public PricePageCursor<ProductDto> readBrandProductsPriceAsc(Principal principal, Integer key, int size, Long brandId, Long categoryId) throws Exception {
        PriceCursorRequest priceCursorRequest = new PriceCursorRequest(key, size);
        var products = productReadService.getBrandProductsByCursorOrderByPriceAsc(priceCursorRequest, brandId, categoryId);
        productReadService.validatePrincipalLike(principal, products.getBody());

        return products;
    }

    @Operation(summary = "브랜드 상품 조회(가격 높은순)", description = "[인증 불필요]")
    @GetMapping("/get/brand/3")
    public PricePageCursor<ProductDto> readBrandProductsPriceDesc(Principal principal, Integer key, int size, Long brandId, Long categoryId) throws Exception {
        PriceCursorRequest priceCursorRequest = new PriceCursorRequest(key, size);
        var products = productReadService.getBrandProductsByCursorOrderByPriceDesc(priceCursorRequest, brandId, categoryId);
        productReadService.validatePrincipalLike(principal, products.getBody());

        return products;
    }
    // ==================================================================================

    @Operation(summary = "주문 창에 사용할 상품들 조회", description = "[인증 필요]")
    @ApiResponse(responseCode = "200", description = "OK")
    @GetMapping("/get/order/product")
    public List<ProductOrderResponse> getProducts(Principal principal, @RequestParam List<Long> productIds){
        User user = userReadService.getUserByEmail(principal.getName());
        return productReadService.getProductsByIds(productIds);
    }
// ==================================================================================================================
// ====================================================== 상품 댓글 =====================================================
    @Operation(summary = "상품 댓글 생성", description = "[인증 필요]")
    @ApiResponse(responseCode = "200", description = "OK")
    @PostMapping("/comment/add")
    public ProductReviewDto createComment(Principal principal, ProductReviewRequest productReviewRequest){
        User user = userReadService.getUserByEmail(principal.getName());
        var comment = productReviewWriteService.createProductReview(user.getId(), productReviewRequest);
        return productReviewReadService.toDto(comment);
    }

    @Operation(summary = "상품의 모든 댓글 조회", description = "[인증 불필요]")
    @ApiResponse(responseCode = "200", description = "OK")
    @GetMapping("/{productId}/comments")
    public List<ProductReviewDto> readAllCommentsByProductId(@PathVariable Long productId){
        return productReviewReadService.getAllComments(productId);
    }

    @Operation(summary = "상품 댓글 삭제", description = "[인증 필요]")
    @ApiResponse(responseCode = "200", description = "OK")
    @DeleteMapping("/{commentId}/comments")
    public void deleteComment(@PathVariable Long commentId, @RequestParam Long userId){
        productReviewWriteService.deleteProductReview(commentId, userId);
    }

    // =================================== Like ================================================
    @Operation(summary = "상품 좋아요", description = "[인증 필요]")
    @ApiResponse(responseCode = "200", description = "OK")
    @PostMapping("/like/{productId}")
    public void productLike(Principal principal, @PathVariable Long productId){
        User user = userReadService.getUserByEmail(principal.getName());
        productLikeWriteService.createOrDeleteProductLike(user, productId);
    }

    @Operation(summary = "상품 리뷰 좋아요", description = "[인증 필요]")
    @ApiResponse(responseCode = "200", description = "OK")
    @PostMapping("/like/review/{productReviewId}")
    public void productReviewLike(Principal principal, @PathVariable Long productReviewId){
        User user = userReadService.getUserByEmail(principal.getName());
        productReviewLikeWriteService.createOrDeleteProductReviewLike(user, productReviewId);
    }
    // ==========================================================================

}
