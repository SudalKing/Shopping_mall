package com.example.shoppingmall.domain.product.product.service;

import com.example.shoppingmall.domain.awsS3.service.ProductImageReadService;
import com.example.shoppingmall.domain.brand.repository.BrandRepository;
import com.example.shoppingmall.domain.product.product.dto.res.ProductReadResponse;
import com.example.shoppingmall.domain.cart.repository.CartProductRepository;
import com.example.shoppingmall.domain.product.product.dto.ProductDto;
import com.example.shoppingmall.domain.product.product.entity.Product;
import com.example.shoppingmall.domain.product.product.repository.ProductLikeRepository;
import com.example.shoppingmall.domain.product.product.repository.ProductRepository;
import com.example.shoppingmall.domain.product_util.entity.ClothesProduct;
import com.example.shoppingmall.domain.product_util.entity.ProductSale;
import com.example.shoppingmall.domain.product_util.repository.ClothesProductRepository;
import com.example.shoppingmall.domain.product_util.repository.ProductSaleRepository;
import com.example.shoppingmall.domain.user.entity.User;
import com.example.shoppingmall.domain.user.service.UserReadService;
import com.example.shoppingmall.util.CursorRequest;
import com.example.shoppingmall.util.PageCursor;
import com.example.shoppingmall.util.product.pagination.PriceCursorRequest;
import com.example.shoppingmall.util.product.pagination.PricePageCursor;
import com.example.shoppingmall.util.product.pagination.ScoreCursorRequest;
import com.example.shoppingmall.util.product.pagination.ScorePageCursor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ProductReadService {
    private final ProductRepository productRepository;
    private final ProductLikeRepository productLikeRepository;
    private final ProductSaleRepository productSaleRepository;
    private final ClothesProductRepository clothesProductRepository;
    private final CartProductRepository cartProductRepository;
    private final BrandRepository brandRepository;

    private final ProductImageReadService productImageReadService;
    private final UserReadService userReadService;


    private final static long NEW_PRODUCT_DAYS = 7;

    private final static Long CLOTHES_TYPE_ID = 1L;
    private final static Long PROP_TYPE_ID = 2L;
    private final static Long GOODS_TYPE_ID = 3L;
    private final static Long HOMELIVING_TYPE_ID = 3L;
    private final static Long BEAUTY_TYPE_ID = 3L;

    public ProductDto getProduct(Long productId) {
        var product = productRepository.findProductById(productId);
        return toDto(product);
    }
    public Product getProductEntity(Long productId){
        return productRepository.findProductById(productId);
    }
    public int readHighestPrice(){
        return productRepository.findTopByOrderByPriceDesc().getPrice();
    }

    public List<Product> getProductsByProductIds(List<Long> productIds) {
        return productRepository.findProductsByIdIn(productIds);
    }

    // Best 조회
    public List<ProductDto> getBestProducts(Long typeId, Long categoryId) {
        List<Product> productList = findBestProducts(typeId, categoryId);

        return productList.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }
    private List<Product> findAllBestProducts() {
        List<Product> allBestProducts = new ArrayList<>();

        List<Product> clothes1 = findBestProducts(1L, 1L);
        List<Product> clothes2 = findBestProducts(1L, 2L);
        List<Product> clothes3 = findBestProducts(1L, 3L);
        List<Product> clothes4 = findBestProducts(1L, 4L);
        List<Product> clothes5 = findBestProducts(1L, 5L);

        List<Product> props = findBestProducts(2L, 0L);
        List<Product> goods = findBestProducts(3L, 0L);
        List<Product> homeLivings = findBestProducts(4L, 0L);
        List<Product> beauty = findBestProducts(5L, 0L);

        allBestProducts.addAll(clothes1);
        allBestProducts.addAll(clothes2);
        allBestProducts.addAll(clothes3);
        allBestProducts.addAll(clothes4);
        allBestProducts.addAll(clothes5);

        allBestProducts.addAll(props);
        allBestProducts.addAll(goods);
        allBestProducts.addAll(homeLivings);
        allBestProducts.addAll(beauty);

        return allBestProducts;
    }
    // typeId == 1 의류, categoryId 는 의류에만 사용 /
    private List<Product> findBestProducts(Long typeId, Long categoryId) {
        if (typeId == 1L) {
            return productRepository.findTop3ByTypeIdAndCategoryIdOrderByStockDesc(typeId, categoryId);
        } else {
            return productRepository.findTop3ByTypeIdOrderByStockDesc(typeId);
        }
    }

    // =================================== 카테고리가 없는 상품 조회 ===========================================================
    public PageCursor<ProductDto> getProductsByCursor(CursorRequest cursorRequest, String tab) throws Exception {
        var products = findProductsNoCategory(cursorRequest, tab);
        var productDtos = products.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
        var nextKey = getNextKey(products);

        return new PageCursor<>(cursorRequest.next(nextKey), productDtos);
    }
    public ScorePageCursor<ProductDto> getProductsByCursorOrderByScore(ScoreCursorRequest scoreCursorRequest, String tab) throws Exception {
        var products = findProductsNoCategoryOrderByScore(scoreCursorRequest, tab);
        var productDtos = products.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
        var nextKey = getScoreNextKey(productDtos);

        return new ScorePageCursor<>(scoreCursorRequest.next(nextKey), productDtos);
    }
    public PricePageCursor<ProductDto> getProductsByCursorOrderByPriceAsc(PriceCursorRequest priceCursorRequest, String tab) throws Exception {
        var products = findProductsNoCategoryOrderByPriceAsc(priceCursorRequest, tab);
        var productDtos = products.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
        var nextKey = getNextPriceAscKey(products);

        return new PricePageCursor<>(priceCursorRequest.next(nextKey), productDtos);
    }
    public PricePageCursor<ProductDto> getProductsByCursorOrderByPriceDesc(PriceCursorRequest priceCursorRequest, String tab) throws Exception {
        var products = findProductsNoCategoryOrderByPriceDesc(priceCursorRequest, tab);
        var productDtos = products.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
        var nextKey = getNextPriceDescKey(products);

        return new PricePageCursor<>(priceCursorRequest.next(nextKey), productDtos);
    }

    private List<Product> findProductsNoCategory(CursorRequest cursorRequest, String tab) throws Exception {
        switch (tab) {
            case "all":
                if (cursorRequest.hasKey()) {
                    return productRepository.findAllProductsByCursorHasKey(cursorRequest.getKey(), cursorRequest.getSize());
                } else {
                    return productRepository.findAllProductsByCursorNoKey(cursorRequest.getSize());
                }
            case "new":
                if (cursorRequest.hasKey()) {
                    return productRepository.findAllNewProductsByCursorHasKey(cursorRequest.getKey(),
                            cursorRequest.getSize(),
                            LocalDateTime.now().minusDays(NEW_PRODUCT_DAYS));
                } else {
                    return productRepository.findAllNewProductsByCursorNoKey(cursorRequest.getSize(),
                            LocalDateTime.now().minusDays(NEW_PRODUCT_DAYS));
                }
            case "sale":
                if (cursorRequest.hasKey()) {
                    return productRepository.findAllSaleProductsHasKey(cursorRequest.getKey(), cursorRequest.getSize());
                } else {
                    return productRepository.findAllSaleProductsNoKey(cursorRequest.getSize());
                }
            case "prop":
                if (cursorRequest.hasKey()) {
                    return productRepository.findAllTypeProductsHasKey(cursorRequest.getKey(), PROP_TYPE_ID, cursorRequest.getSize());
                } else {
                    return productRepository.findAllTypeProductsNoKey(PROP_TYPE_ID, cursorRequest.getSize());
                }
            case "goods":
                if (cursorRequest.hasKey()) {
                    return productRepository.findAllTypeProductsHasKey(cursorRequest.getKey(), GOODS_TYPE_ID, cursorRequest.getSize());
                } else {
                    return productRepository.findAllTypeProductsNoKey(GOODS_TYPE_ID, cursorRequest.getSize());
                }
            case "homeLivings":
                if (cursorRequest.hasKey()) {
                    return productRepository.findAllTypeProductsHasKey(cursorRequest.getKey(), HOMELIVING_TYPE_ID, cursorRequest.getSize());
                } else {
                    return productRepository.findAllTypeProductsNoKey(HOMELIVING_TYPE_ID, cursorRequest.getSize());
                }
            case "beauty":
                if (cursorRequest.hasKey()) {
                    return productRepository.findAllTypeProductsHasKey(cursorRequest.getKey(), BEAUTY_TYPE_ID, cursorRequest.getSize());
                } else {
                    return productRepository.findAllTypeProductsNoKey(BEAUTY_TYPE_ID, cursorRequest.getSize());
                }
            default:
                throw new Exception("!!!");
        }
    }
    private List<Product> findProductsNoCategoryOrderByScore(ScoreCursorRequest scoreCursorRequest, String tab) throws Exception {
        switch (tab) {
            case "all":
                if (scoreCursorRequest.hasKey()) {
                    return productRepository.findAllProductsByCursorHasKeyOrderByScore(scoreCursorRequest.getKey(),
                            scoreCursorRequest.getSize());
                } else {
                    return productRepository.findAllProductsByCursorNoKeyOrderByScore(scoreCursorRequest.getSize());
                }
            case "new":
                if (scoreCursorRequest.hasKey()) {
                    return productRepository.findAllNewProductsByCursorHasKeyOrderByScore(scoreCursorRequest.getKey(),
                            scoreCursorRequest.getSize(),
                            LocalDateTime.now().minusDays(NEW_PRODUCT_DAYS));
                } else {
                    return productRepository.findAllNewProductsByCursorNoKeyOrderByScore(
                            scoreCursorRequest.getSize(),
                            LocalDateTime.now().minusDays(NEW_PRODUCT_DAYS));
                }
            case "sale":
                if (scoreCursorRequest.hasKey()) {
                    return productRepository.findAllSaleProductsHasKeyOrderByScore(scoreCursorRequest.getKey(), scoreCursorRequest.getSize());
                } else {
                    return productRepository.findAllSaleProductsNoKeyOrderByScore(scoreCursorRequest.getSize());
                }
            case "prop":
                if (scoreCursorRequest.hasKey()) {
                    return productRepository.findAllTypeProductsHasKeyOrderByScore(scoreCursorRequest.getKey(), PROP_TYPE_ID, scoreCursorRequest.getSize());
                } else {
                    return productRepository.findAllTypeProductsNoKeyOrderByScore(PROP_TYPE_ID, scoreCursorRequest.getSize());
                }
            case "goods":
                if (scoreCursorRequest.hasKey()) {
                    return productRepository.findAllTypeProductsHasKeyOrderByScore(scoreCursorRequest.getKey(), GOODS_TYPE_ID, scoreCursorRequest.getSize());
                } else {
                    return productRepository.findAllTypeProductsNoKeyOrderByScore(GOODS_TYPE_ID, scoreCursorRequest.getSize());
                }
            case "homeLivings":
                if (scoreCursorRequest.hasKey()) {
                    return productRepository.findAllTypeProductsHasKeyOrderByScore(scoreCursorRequest.getKey(), HOMELIVING_TYPE_ID, scoreCursorRequest.getSize());
                } else {
                    return productRepository.findAllTypeProductsNoKeyOrderByScore(HOMELIVING_TYPE_ID, scoreCursorRequest.getSize());
                }
            case "beauty":
                if (scoreCursorRequest.hasKey()) {
                    return productRepository.findAllTypeProductsHasKeyOrderByScore(scoreCursorRequest.getKey(), BEAUTY_TYPE_ID, scoreCursorRequest.getSize());
                } else {
                    return productRepository.findAllTypeProductsNoKeyOrderByScore(BEAUTY_TYPE_ID, scoreCursorRequest.getSize());
                }
            default:
                throw new Exception("!!!");
        }
    }
    private List<Product> findProductsNoCategoryOrderByPriceAsc(PriceCursorRequest priceCursorRequest, String tab) throws Exception {
        switch (tab) {
            case "all":
                if (priceCursorRequest.hasKey()) {
                    return productRepository.findAllProductsByCursorHasKeyOrderByPriceAsc(priceCursorRequest.getKey(), priceCursorRequest.getSize());
                } else {
                    return productRepository.findAllProductsByCursorNoKeyOrderByPriceAsc(priceCursorRequest.getSize());
                }
            case "new":
                if (priceCursorRequest.hasKey()) {
                    return productRepository.findAllNewProductsByCursorHasKeyOrderByPriceAsc(
                            priceCursorRequest.getKey(),
                            LocalDateTime.now().minusDays(NEW_PRODUCT_DAYS),
                            priceCursorRequest.getSize());
                } else {
                    return productRepository.findAllNewProductsByCursorNoKeyOrderByPriceAsc(
                            LocalDateTime.now().minusDays(NEW_PRODUCT_DAYS),
                            priceCursorRequest.getSize());
                }
            case "sale":
                if (priceCursorRequest.hasKey()) {
                    return productRepository.findAllSaleProductsHasKeyOrderByPriceAsc(priceCursorRequest.getKey(), priceCursorRequest.getSize());
                } else {
                    return productRepository.findAllSaleProductsNoKeyOrderByPriceAsc(priceCursorRequest.getSize());
                }
            case "prop":
                if (priceCursorRequest.hasKey()) {
                    return productRepository.findAllTypeProductsHasKeyOrderByPriceAsc(priceCursorRequest.getKey(), PROP_TYPE_ID, priceCursorRequest.getSize());
                } else {
                    return productRepository.findAllTypeProductsNoKeyOrderByPriceAsc(PROP_TYPE_ID, priceCursorRequest.getSize());
                }
            case "goods":
                if (priceCursorRequest.hasKey()) {
                    return productRepository.findAllTypeProductsHasKeyOrderByPriceAsc(priceCursorRequest.getKey(), GOODS_TYPE_ID, priceCursorRequest.getSize());
                } else {
                    return productRepository.findAllTypeProductsNoKeyOrderByPriceAsc(GOODS_TYPE_ID, priceCursorRequest.getSize());
                }
            case "homeLivings":
                if (priceCursorRequest.hasKey()) {
                    return productRepository.findAllTypeProductsHasKeyOrderByPriceAsc(priceCursorRequest.getKey(), HOMELIVING_TYPE_ID, priceCursorRequest.getSize());
                } else {
                    return productRepository.findAllTypeProductsNoKeyOrderByPriceAsc(HOMELIVING_TYPE_ID, priceCursorRequest.getSize());
                }
            case "beauty":
                if (priceCursorRequest.hasKey()) {
                    return productRepository.findAllTypeProductsHasKeyOrderByPriceAsc(priceCursorRequest.getKey(), BEAUTY_TYPE_ID, priceCursorRequest.getSize());
                } else {
                    return productRepository.findAllTypeProductsNoKeyOrderByPriceAsc(BEAUTY_TYPE_ID, priceCursorRequest.getSize());
                }
            default:
                throw new Exception("1!!");
        }
    }
    private List<Product> findProductsNoCategoryOrderByPriceDesc(PriceCursorRequest priceCursorRequest, String tab) throws Exception {
        switch (tab) {
            case "all":
                if (priceCursorRequest.hasKey()) {
                    return productRepository.findAllProductsByCursorHasKeyOrderByPriceDesc(priceCursorRequest.getKey(), priceCursorRequest.getSize());
                } else {
                    return productRepository.findAllProductsByCursorNoKeyOrderByPriceDesc(priceCursorRequest.getSize());
                }
            case "new":
                if (priceCursorRequest.hasKey()) {
                    return productRepository.findAllNewProductsByCursorHasKeyOrderByPriceDesc(
                            priceCursorRequest.getKey(),
                            LocalDateTime.now().minusDays(NEW_PRODUCT_DAYS),
                            priceCursorRequest.getSize());
                } else {
                    return productRepository.findAllNewProductsByCursorNoKeyOrderByPriceDesc(
                            LocalDateTime.now().minusDays(NEW_PRODUCT_DAYS),
                            priceCursorRequest.getSize());
                }
            case "sale":
                if (priceCursorRequest.hasKey()) {
                    return productRepository.findAllSaleProductsHasKeyOrderByPriceDesc(priceCursorRequest.getKey(), priceCursorRequest.getSize());
                } else {
                    return productRepository.findAllSaleProductsNoKeyOrderByPriceDesc(priceCursorRequest.getSize());
                }
            case "prop":
                if (priceCursorRequest.hasKey()) {
                    return productRepository.findAllTypeProductsHasKeyOrderByPriceDesc(priceCursorRequest.getKey(), PROP_TYPE_ID, priceCursorRequest.getSize());
                } else {
                    return productRepository.findAllTypeProductsNoKeyOrderByPriceDesc(PROP_TYPE_ID, priceCursorRequest.getSize());
                }
            case "goods":
                if (priceCursorRequest.hasKey()) {
                    return productRepository.findAllTypeProductsHasKeyOrderByPriceDesc(priceCursorRequest.getKey(), GOODS_TYPE_ID, priceCursorRequest.getSize());
                } else {
                    return productRepository.findAllTypeProductsNoKeyOrderByPriceDesc(GOODS_TYPE_ID, priceCursorRequest.getSize());
                }
            case "homeLivings":
                if (priceCursorRequest.hasKey()) {
                    return productRepository.findAllTypeProductsHasKeyOrderByPriceDesc(priceCursorRequest.getKey(), HOMELIVING_TYPE_ID, priceCursorRequest.getSize());
                } else {
                    return productRepository.findAllTypeProductsNoKeyOrderByPriceDesc(HOMELIVING_TYPE_ID, priceCursorRequest.getSize());
                }
            case "beauty":
                if (priceCursorRequest.hasKey()) {
                    return productRepository.findAllTypeProductsHasKeyOrderByPriceDesc(priceCursorRequest.getKey(), BEAUTY_TYPE_ID, priceCursorRequest.getSize());
                } else {
                    return productRepository.findAllTypeProductsNoKeyOrderByPriceDesc(BEAUTY_TYPE_ID, priceCursorRequest.getSize());
                }
            default:
                throw new Exception("1!!");
        }
    }
// =========================================================================================================================

// =========================================== 카테고리가 존재하는 상품 ==========================================================
    public PageCursor<ProductDto> getProductsByCursorHasCategory(CursorRequest cursorRequest, String tab, Long categoryId) throws Exception {
        List<Product> products = findProductsHasCategory(cursorRequest, tab, categoryId);
        List<ProductDto> productDtos = products.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
        var nextKey = getNextKey(products);

        return new PageCursor<>(cursorRequest.next(nextKey), productDtos);
    }
    public ScorePageCursor<ProductDto> getProductsByCursorHasCategoryOrderByScore(ScoreCursorRequest scoreCursorRequest, String tab, Long categoryId) throws Exception {
        List<Product> products = findProductsHasCategoryOrderByScore(scoreCursorRequest, tab, categoryId);
        List<ProductDto> productDtos = products.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
        var nextKey = getScoreNextKey(productDtos);

        return new ScorePageCursor<>(scoreCursorRequest.next(nextKey), productDtos);
    }
    public PricePageCursor<ProductDto> getProductsByCursorHasCategoryOrderByPriceAsc(PriceCursorRequest priceCursorRequest, String tab, Long categoryId) throws Exception {
        List<Product> products = findProductsHasCategoryOrderByPriceAsc(priceCursorRequest, tab, categoryId);
        List<ProductDto> productDtos = products.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
        var nextKey = getNextPriceAscKey(products);

        return new PricePageCursor<>(priceCursorRequest.next(nextKey), productDtos);
    }
    public PricePageCursor<ProductDto> getProductsByCursorHasCategoryOrderByPriceDesc(PriceCursorRequest priceCursorRequest, String tab, Long categoryId) throws Exception {
        List<Product> products = findProductsHasCategoryOrderByPriceDesc(priceCursorRequest, tab, categoryId);
        List<ProductDto> productDtos = products.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
        var nextKey = getNextPriceDescKey(products);

        return new PricePageCursor<>(priceCursorRequest.next(nextKey), productDtos);
    }

    private List<Product> findProductsHasCategory(CursorRequest cursorRequest, String tab, Long categoryId) throws Exception {
        switch (tab) {
            case "clothes":
                if (categoryId == 0L) {
                    if (cursorRequest.hasKey()) {
                        return productRepository.findAllTypeProductsHasKey(cursorRequest.getKey(), CLOTHES_TYPE_ID, cursorRequest.getSize());
                    } else {
                        return productRepository.findAllTypeProductsNoKey(CLOTHES_TYPE_ID, cursorRequest.getSize());
                    }
                } else {
                    if (cursorRequest.hasKey()) {
                        return productRepository.findAllTypeCategoryProductsHasKey(cursorRequest.getKey(), CLOTHES_TYPE_ID, categoryId, cursorRequest.getSize());
                    } else {
                        return productRepository.findAllTypeCategoryProductsNoKey(CLOTHES_TYPE_ID, categoryId, cursorRequest.getSize());
                    }
                }
            case "new":
            default:
                throw new Exception("1!!");
        }
    }
    private List<Product> findProductsHasCategoryOrderByScore(ScoreCursorRequest scoreCursorRequest, String tab, Long categoryId) throws Exception {
        switch (tab) {
            case "clothes":
                if (categoryId == 0L) {
                    if (scoreCursorRequest.hasKey()) {
                        return productRepository.findAllTypeProductsHasKeyOrderByScore(scoreCursorRequest.getKey(), CLOTHES_TYPE_ID, scoreCursorRequest.getSize());
                    } else {
                        return productRepository.findAllTypeProductsNoKeyOrderByScore(CLOTHES_TYPE_ID, scoreCursorRequest.getSize());
                    }
                } else {
                    if (scoreCursorRequest.hasKey()) {
                        return productRepository.findTypeCategoryProductsHasKeyOrderByScore(scoreCursorRequest.getKey(), CLOTHES_TYPE_ID, categoryId, scoreCursorRequest.getSize());
                    } else {
                        return productRepository.findTypeCategoryProductsNoKeyOrderByScore(CLOTHES_TYPE_ID, categoryId, scoreCursorRequest.getSize());
                    }
                }
            case "new":
            default:
                throw new Exception("1!!");
        }
    }
    private List<Product> findProductsHasCategoryOrderByPriceAsc(PriceCursorRequest priceCursorRequest, String tab, Long categoryId) throws Exception {
        switch (tab) {
            case "clothes":
                if (categoryId == 0L) {
                    if (priceCursorRequest.hasKey()) {
                        return productRepository.findAllTypeProductsHasKeyOrderByPriceAsc(priceCursorRequest.getKey(), CLOTHES_TYPE_ID, priceCursorRequest.getSize());
                    } else {
                        return productRepository.findAllTypeProductsNoKeyOrderByPriceAsc(CLOTHES_TYPE_ID, priceCursorRequest.getSize());
                    }
                } else {
                    if (priceCursorRequest.hasKey()) {
                        return productRepository.findTypeCategoryProductsHasKeyOrderByPriceAsc(priceCursorRequest.getKey(), CLOTHES_TYPE_ID, categoryId, priceCursorRequest.getSize());
                    } else {
                        return productRepository.findTypeCategoryProductsNoKeyOrderByPriceAsc(CLOTHES_TYPE_ID, categoryId, priceCursorRequest.getSize());
                    }
                }
            case "new":
            default:
                throw new Exception("1!!");
        }
    }
    private List<Product> findProductsHasCategoryOrderByPriceDesc(PriceCursorRequest priceCursorRequest, String tab, Long categoryId) throws Exception {
        if (tab.equals("clothes")) {
            if (categoryId == 0L) {
                if (priceCursorRequest.hasKey()) {
                    return productRepository.findAllTypeProductsHasKeyOrderByPriceDesc(priceCursorRequest.getKey(), CLOTHES_TYPE_ID, priceCursorRequest.getSize());
                } else {
                    return productRepository.findAllTypeProductsNoKeyOrderByPriceDesc(CLOTHES_TYPE_ID, priceCursorRequest.getSize());
                }
            } else {
                if (priceCursorRequest.hasKey()) {
                    return productRepository.findTypeCategoryProductsHasKeyOrderByPriceDesc(priceCursorRequest.getKey(), CLOTHES_TYPE_ID, categoryId, priceCursorRequest.getSize());
                } else {
                    return productRepository.findTypeCategoryProductsNoKeyOrderByPriceDesc(CLOTHES_TYPE_ID, categoryId, priceCursorRequest.getSize());
                }
            }
        }
        throw new Exception("1!!");
    }
// ================================================================================================================================
// ================================================== 브랜드 상품 조회 =======================================================
    public PageCursor<ProductDto> getBrandProductsByCursor(CursorRequest cursorRequest, Long brandId, Long categoryId) throws Exception {
        List<Product> products = findBrandProducts(cursorRequest, brandId, categoryId);
        List<ProductDto> productDtos = products.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
        var nextKey = getNextKey(products);

        return new PageCursor<>(cursorRequest.next(nextKey), productDtos);
    }
    public ScorePageCursor<ProductDto> getBrandProductsByCursorOrderByScore(ScoreCursorRequest scoreCursorRequest, Long brandId, Long categoryId) throws Exception {
        List<Product> products = findBrandProductsOrderByScore(scoreCursorRequest, brandId, categoryId);
        List<ProductDto> productDtos = products.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
        var nextKey = getScoreNextKey(productDtos);

        return new ScorePageCursor<>(scoreCursorRequest.next(nextKey), productDtos);
    }
    public PricePageCursor<ProductDto> getBrandProductsByCursorOrderByPriceAsc(PriceCursorRequest priceCursorRequest, Long brandId, Long categoryId) throws Exception {
        List<Product> products = findBrandProductsOrderByPriceAsc(priceCursorRequest, brandId, categoryId);
        List<ProductDto> productDtos = products.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
        var nextKey = getNextPriceAscKey(products);

        return new PricePageCursor<>(priceCursorRequest.next(nextKey), productDtos);
    }
    public PricePageCursor<ProductDto> getBrandProductsByCursorOrderByPriceDesc(PriceCursorRequest priceCursorRequest, Long brandId, Long categoryId) throws Exception {
        List<Product> products = findBrandProductsOrderByPriceDesc(priceCursorRequest, brandId, categoryId);
        List<ProductDto> productDtos = products.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
        var nextKey = getNextPriceDescKey(products);

        return new PricePageCursor<>(priceCursorRequest.next(nextKey), productDtos);
    }

    private List<Product> findBrandProducts(CursorRequest cursorRequest, Long brandId, Long categoryId) {
        if (categoryId == 0L) {
            if (cursorRequest.hasKey()) {
                return productRepository.findBrandProductsByBrandIdHasKey(cursorRequest.getKey(), brandId, cursorRequest.getSize());
            } else {
                return productRepository.findBrandProductsByBrandIdNoKey(brandId, cursorRequest.getSize());
            }
        } else {
            if (cursorRequest.hasKey()) {
                return productRepository.findBrandProductsByBrandIdAndCategoryIdHasKey(cursorRequest.getKey(), categoryId, brandId, cursorRequest.getSize());
            } else {
                return productRepository.findBrandProductsByBrandIdAndCategoryIdNoKey(categoryId, brandId, cursorRequest.getSize());
            }
        }
    }
    private List<Product> findBrandProductsOrderByScore(ScoreCursorRequest scoreCursorRequest, Long brandId, Long categoryId) {
        if (categoryId == 0L) {
            if (scoreCursorRequest.hasKey()) {
                return productRepository.findBrandProductsByBrandIdHasKeyOrderByScore(scoreCursorRequest.getKey(), brandId, scoreCursorRequest.getSize());
            } else {
                return productRepository.findBrandProductsByBrandIdNoKeyOrderByScore(brandId, scoreCursorRequest.getSize());
            }
        } else {
            if (scoreCursorRequest.hasKey()) {
                return productRepository.findBrandProductsByBrandIdAndCategoryIdHasKeyOrderByScore(scoreCursorRequest.getKey(), categoryId, brandId, scoreCursorRequest.getSize());
            } else {
                return productRepository.findBrandProductsByBrandIdAndCategoryIdNoKeyOrderByScore(categoryId, brandId, scoreCursorRequest.getSize());
            }
        }
    }
    private List<Product> findBrandProductsOrderByPriceAsc(PriceCursorRequest priceCursorRequest, Long brandId, Long categoryId) {
        if (categoryId == 0L) {
            if (priceCursorRequest.hasKey()) {
                return productRepository.findBrandProductsByBrandIdHasKeyOrderByPriceAsc(priceCursorRequest.getKey(), brandId, priceCursorRequest.getSize());
            } else {
                return productRepository.findBrandProductsByBrandIdNoKeyOrderByPriceAsc(brandId, priceCursorRequest.getSize());
            }
        } else {
            if (priceCursorRequest.hasKey()) {
                return productRepository.findBrandProductsByBrandIdAndCategoryIdHasKeyOrderByPriceAsc(priceCursorRequest.getKey(), categoryId, brandId, priceCursorRequest.getSize());
            } else {
                return productRepository.findBrandProductsByBrandIdAndCategoryIdNoKeyOrderByPriceAsc(categoryId, brandId, priceCursorRequest.getSize());
            }
        }
    }
    private List<Product> findBrandProductsOrderByPriceDesc(PriceCursorRequest priceCursorRequest, Long brandId, Long categoryId) {
        if (categoryId == 0L) {
            if (priceCursorRequest.hasKey()) {
                return productRepository.findBrandProductsByBrandIdHasKeyOrderByPriceDesc(priceCursorRequest.getKey(), brandId, priceCursorRequest.getSize());
            } else {
                return productRepository.findBrandProductsByBrandIdNoKeyOrderByPriceDesc(brandId, priceCursorRequest.getSize());
            }
        } else {
            if (priceCursorRequest.hasKey()) {
                return productRepository.findBrandProductsByBrandIdAndCategoryIdHasKeyOrderByPriceDesc(priceCursorRequest.getKey(), categoryId, brandId, priceCursorRequest.getSize());
            } else {
                return productRepository.findBrandProductsByBrandIdAndCategoryIdNoKeyOrderByPriceDesc(categoryId, brandId, priceCursorRequest.getSize());
            }
        }
    }
// ================================================================================================================

    public List<ProductReadResponse> getProductsByIds(List<Long> productIds) {
        List<Product> products = productRepository.findProductsByIdIn(productIds);
        return products.stream()
                .map(this::toProductReadResponse)
                .collect(Collectors.toList());
    }

    public void validatePrincipalLike(Principal principal, List<ProductDto> cursorBody){
        if (principal != null) {
            Optional<User> userOptional = userReadService.getUserPrincipal(principal.getName());
            if (userOptional.isPresent()) {
                User user = userOptional.get();
                updateLikeTrue(user, cursorBody);
            }
        }
    }

    public void updateLikeTrue(User user, List<ProductDto> productDtoList) {
        productDtoList.forEach(
                productDto -> {
                    if (productLikeRepository.findByUserIdAndProductId(user.getId(), productDto.getId()).isPresent()) {
                        productDto.setLiked();
                    }
                }
        );
    }
    public ProductDto toDto(Product product){
        return ProductDto.builder()
                .id(product.getId())
                .name(product.getName())
                .price(product.getPrice())
                .score(getProductScore(product.getId()))
                .description(product.getDescription())
                .imageUrl(productImageReadService.getUrl(product.getId()))
                .isLiked(false)
                .brandInfo(brandRepository.findBrandInfoByProductId(product.getId()))
                .build();
    }


    private Long getNextKey(List<Product> products){
        return products.stream()
                .mapToLong(Product::getId)
                .min()
                .orElse(CursorRequest.NONE_KEY);
    }
    private Double getScoreNextKey(List<ProductDto> productDtos){
        return productDtos.stream()
                .mapToDouble(ProductDto::getScore)
                .min()
                .orElse(ScoreCursorRequest.NONE_KEY);
    }
    private Integer getNextPriceAscKey(List<Product> products){
        return products.stream()
                .mapToInt(Product::getPrice)
                .max()
                .orElse(PriceCursorRequest.NONE_KEY);
    }
    private Integer getNextPriceDescKey(List<Product> products){
        return products.stream()
                .mapToInt(Product::getPrice)
                .min()
                .orElse(PriceCursorRequest.NONE_KEY);
    }


    private Long getBrandCategoryId(Long productId) {
        return productRepository.findBrandCategoryId(productId);
    }

    public Map<String, String> getClothesSizeAndColor(Product product) {
        Long typeId = product.getTypeId();
        Map<String, String> clothesInfo = new HashMap<>();
        ClothesProduct clothesProduct = clothesProductRepository.findByProductId(product.getId());

        if (typeId == 1L) {
            clothesInfo.put("color", clothesProduct.getColor());
            clothesInfo.put("size", clothesProduct.getClotheSize());
        } else {
            clothesInfo.put("color", "");
            clothesInfo.put("size", "");
        }

        return clothesInfo;
    }
    private Double getProductScore(Long productId) {
        return productRepository.findProductScore(productId);
    }


    public ProductReadResponse toProductReadResponse(Product product) {
        Map<String, String> clothesInfo = getClothesSizeAndColor(product);
        return new ProductReadResponse(
                product.getId(),
                product.getName(),
                productImageReadService.getUrl(product.getId()),
                clothesInfo.get("color"),
                clothesInfo.get("size"),
                product.getPrice(),
                cartProductRepository.findAmountByProductId(product.getId()),
                getDiscountPrice(product)
        );
    }

    private int getDiscountPrice(Product product){
        if (product.isSaled()) {
            ProductSale productSale = productSaleRepository.findByProductId(product.getId());
            return (int) Math.round(product.getPrice() * productSale.getDiscountRate());
        } else {
            return 0;
        }
    }
}
