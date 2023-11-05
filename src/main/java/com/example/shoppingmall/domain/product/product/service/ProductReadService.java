package com.example.shoppingmall.domain.product.product.service;

import com.example.shoppingmall.domain.awsS3.service.ProductImageReadService;
import com.example.shoppingmall.domain.brand.repository.BrandRepository;
import com.example.shoppingmall.domain.product.product.dto.res.ProductReadResponse;
import com.example.shoppingmall.domain.cart.repository.CartProductRepository;
import com.example.shoppingmall.domain.product.product.dto.ProductResponse;
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


    public ProductResponse getProduct(Long productId) {
        var product = productRepository.findProductById(productId);
        return toDto(product);
    }
    public Product getProductEntity(Long productId){
        return productRepository.findProductById(productId);
    }
    public int readHighestPrice(){
        return productRepository.findTopByOrderByPriceDesc().getPrice();
    }
    public List<Product> getProductsByProductIds(List<Long> productIds) {return productRepository.findProductsByIdIn(productIds);}

    // ============================ Best 조회 ===================================================
//    public List<ProductResponse> getAllBestProducts() {
//        List<Product> productList = findAllBestProducts();
//
//        return productList.stream()
//                .map(this::toDto)
//                .collect(Collectors.toList());
//    }

//    public List<ProductResponse> getBestProducts(Long typeId, Long categoryId) {
//        List<Product> productList = findBestProducts(typeId, categoryId);
//
//        return productList.stream()
//                .map(this::toDto)
//                .collect(Collectors.toList());
//    }
    // typeId == 1 의류, categoryId 는 의류에만 사용 /
//    private List<Product> findBestProducts(Long typeId, Long categoryId) {
//        if (typeId == 1L) {
//            return productRepository.findTop3ByTypeIdAndCategoryIdOrderByStockDesc(typeId, categoryId);
//        } else {
//            return productRepository.findTop3ByTypeIdOrderByStockDesc(typeId);
//        }
//    }

    // ============================================ 전체 상품 조회 =================================================
    public PageCursor<ProductResponse> getProductsByCursor(Number key, int size, Long sortId) throws Exception {
        CursorRequest cursorRequest = new CursorRequest(key, size);

        var products = findAll(cursorRequest, sortId);
        return getProductResponsePageCursor(cursorRequest, products, sortId);
    }

    public PageCursor<ProductResponse> getNewProductsByCursor(Number key, int size, Long sortId) throws Exception {
        CursorRequest cursorRequest = new CursorRequest(key, size);

        var products = findNewAll(cursorRequest, sortId);
        return getProductResponsePageCursor(cursorRequest, products, sortId);
    }

    public PageCursor<ProductResponse> getSaleProductsByCursor(Number key, int size, Long sortId) throws Exception {
        CursorRequest cursorRequest = new CursorRequest(key, size);

        var products = findSaleAll(cursorRequest, sortId);
        return getProductResponsePageCursor(cursorRequest, products, sortId);
    }

    public PageCursor<ProductResponse> getProductsByCursorByCategoryAndSubCategoryId(Number key, int size, Long sortId, Long categoryId, Long subCategoryId) throws Exception {
        CursorRequest cursorRequest = new CursorRequest(key, size);

        var products = findCategoryAndSubCategoryAll(cursorRequest, sortId, categoryId, subCategoryId);
        return getProductResponsePageCursor(cursorRequest, products, sortId);
    }

    public PageCursor<ProductResponse> getBrandProductsByCursor(Number key, int size, Long sortId, Long brandId) throws Exception {
        CursorRequest cursorRequest = new CursorRequest(key, size);

        var products = findBrandAll(cursorRequest, sortId, brandId);
        return getProductResponsePageCursor(cursorRequest, products, sortId);
    }

    public PageCursor<ProductResponse> getBrandCategoryProductsByCursor(Number key, int size, Long sortId, Long brandId, Long categoryId, Long subCategoryId) throws Exception {
        CursorRequest cursorRequest = new CursorRequest(key, size);

        var products = findBrandCategoryAll(cursorRequest, sortId, brandId, categoryId, subCategoryId);
        return getProductResponsePageCursor(cursorRequest, products, sortId);
    }





    private List<Product> findAll(CursorRequest cursorRequest, Long sortId) throws Exception {
        if (sortId == 0L) {
            if (cursorRequest.hasKey()) {
                return productRepository.findAllOrderByIdDescHasKey(cursorRequest.getKey().longValue(), cursorRequest.getSize());
            } else {
                return productRepository.findAllOrderByIdDescNoKey(cursorRequest.getSize());
            }
        } else if (sortId == 1L) {
            if (cursorRequest.hasKey()) {
                return productRepository.findAllOrderByScoreHasKey(cursorRequest.getKey().doubleValue(), cursorRequest.getSize());
            } else {
                return productRepository.findAllOrderByScoreNoKey(cursorRequest.getSize());
            }
        } else if (sortId == 2L) {
            if (cursorRequest.hasKey()) {
                return productRepository.findAllOrderByPriceAscHasKey(cursorRequest.getKey().intValue(), cursorRequest.getSize());
            } else {
                return productRepository.findAllOrderByPriceAscNoKey(cursorRequest.getSize());
            }
        } else if (sortId == 3L){
            if (cursorRequest.hasKey()) {
                return productRepository.findAllOrderByPriceDescHasKey(cursorRequest.getKey().intValue(), cursorRequest.getSize());
            } else {
                return productRepository.findAllOrderByPriceDescNoKey(cursorRequest.getSize());
            }
        } else {
            throw new Exception("Wrong SortId!!");
        }
    }

    private List<Product> findNewAll(CursorRequest cursorRequest, Long sortId) throws Exception {
        if (sortId == 0L) {
            if (cursorRequest.hasKey()) {
                return productRepository.findAllNewOrderByIdDescHasKey(
                        cursorRequest.getKey().longValue(),
                        cursorRequest.getSize(),
                        LocalDateTime.now().minusDays(NEW_PRODUCT_DAYS));
            } else {
                return productRepository.findAllNewOrderByIdDescNoKey(
                        cursorRequest.getSize(),
                        LocalDateTime.now().minusDays(NEW_PRODUCT_DAYS));
            }
        } else if (sortId == 1L) {
            if (cursorRequest.hasKey()) {
                return productRepository.findAllNewOrderByScoreHasKey(
                        cursorRequest.getKey().doubleValue(),
                        cursorRequest.getSize(),
                        LocalDateTime.now().minusDays(NEW_PRODUCT_DAYS));
            } else {
                return productRepository.findAllNewOrderByIdDescNoKey(
                        cursorRequest.getSize(),
                        LocalDateTime.now().minusDays(NEW_PRODUCT_DAYS));
            }
        } else if (sortId == 2L) {
            if (cursorRequest.hasKey()) {
                return productRepository.findAllNewOrderByPriceAscHasKey(
                        cursorRequest.getKey().intValue(),
                        cursorRequest.getSize(),
                        LocalDateTime.now().minusDays(NEW_PRODUCT_DAYS));
            } else {
                return productRepository.findAllNewOrderByPriceAscNoKey(
                        cursorRequest.getSize(),
                        LocalDateTime.now().minusDays(NEW_PRODUCT_DAYS));
            }
        } else if (sortId == 3L){
            if (cursorRequest.hasKey()) {
                return productRepository.findAllNewOrderByPriceDescHasKey(
                        cursorRequest.getKey().intValue(),
                        cursorRequest.getSize(),
                        LocalDateTime.now().minusDays(NEW_PRODUCT_DAYS));
            } else {
                return productRepository.findAllNewOrderByPriceDescNoKey(
                        cursorRequest.getSize(),
                        LocalDateTime.now().minusDays(NEW_PRODUCT_DAYS));
            }
        } else {
            throw new Exception("Wrong SortId!!");
        }
    }

    private List<Product> findSaleAll(CursorRequest cursorRequest, Long sortId) throws Exception {
        if (sortId == 0L) {
            if (cursorRequest.hasKey()) {
                return productRepository.findAllSaleOrderByIdDescHasKey(cursorRequest.getKey().longValue(), cursorRequest.getSize());
            } else {
                return productRepository.findAllSaleOrderByIdDescNoKey(cursorRequest.getSize());
            }
        } else if (sortId == 1L) {
            if (cursorRequest.hasKey()) {
                return productRepository.findAllSaleOrderByScoreHasKey(cursorRequest.getKey().doubleValue(), cursorRequest.getSize());
            } else {
                return productRepository.findAllSaleOrderByScoreNoKey(cursorRequest.getSize());
            }
        } else if (sortId == 2L) {
            if (cursorRequest.hasKey()) {
                return productRepository.findAllSaleOrderByPriceAscHasKey(cursorRequest.getKey().intValue(), cursorRequest.getSize());
            } else {
                return productRepository.findAllSaleOrderByPriceAscNoKey(cursorRequest.getSize());
            }
        } else if (sortId == 3L){
            if (cursorRequest.hasKey()) {
                return productRepository.findAllSaleOrderByPriceDescHasKey(cursorRequest.getKey().intValue(), cursorRequest.getSize());
            } else {
                return productRepository.findAllSaleOrderByPriceDescNoKey(cursorRequest.getSize());
            }
        } else {
            throw new Exception("Wrong SortId!!");
        }
    }

    private List<Product> findCategoryAndSubCategoryAll(CursorRequest cursorRequest, Long sortId, Long categoryId, Long subCategoryId) throws Exception {
        if (subCategoryId == 0L) {
            if (sortId == 0L) {
                if (cursorRequest.hasKey()) {
                    return productRepository.findAllByCategoryIdOrderByIdDescHasKey(cursorRequest.getKey().longValue(), categoryId, cursorRequest.getSize());
                } else {
                    return productRepository.findAllByCategoryIdOrderByIdDescNoKey(categoryId, cursorRequest.getSize());
                }
            } else if (sortId == 1L) {
                if (cursorRequest.hasKey()) {
                    return productRepository.findAllByCategoryIdOrderByScoreHasKey(cursorRequest.getKey().doubleValue(), categoryId, cursorRequest.getSize());
                } else {
                    return productRepository.findAllByCategoryIdOrderByScoreNoKey(categoryId, cursorRequest.getSize());
                }
            } else if (sortId == 2L) {
                if (cursorRequest.hasKey()) {
                    return productRepository.findAllByCategoryIdOrderByPriceAscHasKey(cursorRequest.getKey().intValue(), categoryId, cursorRequest.getSize());
                } else {
                    return productRepository.findAllByCategoryIdOrderByPriceAscNoKey(categoryId, cursorRequest.getSize());
                }
            } else if (sortId == 3L) {
                if (cursorRequest.hasKey()) {
                    return productRepository.findAllByCategoryIdOrderByPriceDescHasKey(cursorRequest.getKey().intValue(), categoryId, cursorRequest.getSize());
                } else {
                    return productRepository.findAllByCategoryIdOrderByPriceDescNoKey(categoryId, cursorRequest.getSize());
                }
            } else {
                throw new Exception("Wrong SortId!!");
            }
        } else {
            if (sortId == 0L) {
                if (cursorRequest.hasKey()) {
                    return productRepository.findAllByCategoryAndSubCategoryIdOrderByIdDescHasKey(
                            cursorRequest.getKey().longValue(), categoryId, subCategoryId, cursorRequest.getSize());
                } else {
                    return productRepository.findAllByCategoryAndSubCategoryIdOrderByIdDescNoKey(categoryId, subCategoryId, cursorRequest.getSize());
                }
            } else if (sortId == 1L) {
                if (cursorRequest.hasKey()) {
                    return productRepository.findAllByCategoryAndSubCategoryIdOrderByScoreHasKey(
                            cursorRequest.getKey().doubleValue(), categoryId, subCategoryId, cursorRequest.getSize());
                } else {
                    return productRepository.findAllByCategoryAndSubCategoryIdOrderByScoreNoKey(categoryId, subCategoryId, cursorRequest.getSize());
                }
            } else if (sortId == 2L) {
                if (cursorRequest.hasKey()) {
                    return productRepository.findAllByCategoryAndSubCategoryIdOrderByPriceAscHasKey(
                            cursorRequest.getKey().intValue(), categoryId, subCategoryId, cursorRequest.getSize());
                } else {
                    return productRepository.findAllByCategoryAndSubCategoryIdOrderByPriceAscNoKey(categoryId, subCategoryId, cursorRequest.getSize());
                }
            } else if (sortId == 3L) {
                if (cursorRequest.hasKey()) {
                    return productRepository.findAllByCategoryAndSubCategoryIdOrderByPriceDescHasKey(
                            cursorRequest.getKey().intValue(), categoryId, subCategoryId, cursorRequest.getSize());
                } else {
                    return productRepository.findAllByCategoryAndSubCategoryIdOrderByPriceDescNoKey(categoryId, subCategoryId, cursorRequest.getSize());
                }
            } else {
                throw new Exception("Wrong SortId!!");
            }
        }
    }

    private List<Product> findBrandAll(CursorRequest cursorRequest, Long sortId, Long brandId) throws Exception {
        if (sortId == 0L) {
            if (cursorRequest.hasKey()) {
                return productRepository.findAllByBrandOrderByIdDescHasKey(
                        cursorRequest.getKey().longValue(), brandId, cursorRequest.getSize());
            } else {
                return productRepository.findAllByBrandOrderByIdDescNoKey(brandId, cursorRequest.getSize());
            }
        } else if (sortId == 1L) {
            if (cursorRequest.hasKey()) {
                return productRepository.findAllByBrandOrderByScoreHasKey(
                        cursorRequest.getKey().doubleValue(), brandId, cursorRequest.getSize());
            } else {
                return productRepository.findAllByBrandOrderByScoreNoKey(brandId, cursorRequest.getSize());
            }
        } else if (sortId == 2L) {
            if (cursorRequest.hasKey()) {
                return productRepository.findAllByBrandOrderByPriceAscHasKey(
                        cursorRequest.getKey().intValue(), brandId, cursorRequest.getSize());
            } else {
                return productRepository.findAllByBrandOrderByPriceAscNoKey(brandId, cursorRequest.getSize());
            }
        } else if (sortId == 3L){
            if (cursorRequest.hasKey()) {
                return productRepository.findAllByBrandOrderByPriceDescHasKey(
                        cursorRequest.getKey().intValue(), brandId, cursorRequest.getSize());
            } else {
                return productRepository.findAllByBrandOrderByPriceDescNoKey(brandId, cursorRequest.getSize());
            }
        } else {
            throw new Exception("Wrong SortId!!");
        }
    }

    private List<Product> findBrandCategoryAll(CursorRequest cursorRequest, Long sortId, Long brandId, Long categoryId, Long subCategoryId) throws Exception {
        if (sortId == 0L) {
            if (cursorRequest.hasKey()) {
                return productRepository.findAllByBrandCategoryOrderByIdDescHasKey(
                        cursorRequest.getKey().longValue(), brandId, categoryId, subCategoryId, cursorRequest.getSize());
            } else {
                return productRepository.findAllByBrandCategoryOrderByIdDescNoKey(
                        brandId, categoryId, subCategoryId, cursorRequest.getSize());
            }
        } else if (sortId == 1L) {
            if (cursorRequest.hasKey()) {
                return productRepository.findAllByBrandCategoryOrderByScoreHasKey(
                        cursorRequest.getKey().doubleValue(), brandId, categoryId, subCategoryId, cursorRequest.getSize());
            } else {
                return productRepository.findAllByBrandCategoryOrderByScoreNoKey(
                        brandId, categoryId, subCategoryId, cursorRequest.getSize());
            }
        } else if (sortId == 2L) {
            if (cursorRequest.hasKey()) {
                return productRepository.findAllByBrandCategoryOrderByPriceAscHasKey(
                        cursorRequest.getKey().intValue(), brandId, categoryId, subCategoryId, cursorRequest.getSize());
            } else {
                return productRepository.findAllByBrandCategoryOrderByPriceAscNoKey(
                        brandId, categoryId, subCategoryId, cursorRequest.getSize());
            }
        } else if (sortId == 3L){
            if (cursorRequest.hasKey()) {
                return productRepository.findAllByBrandCategoryOrderByPriceDescHasKey(
                        cursorRequest.getKey().intValue(), brandId, categoryId, subCategoryId, cursorRequest.getSize());
            } else {
                return productRepository.findAllByBrandCategoryOrderByPriceDescNoKey(
                        brandId, categoryId, subCategoryId, cursorRequest.getSize());
            }
        } else {
            throw new Exception("Wrong SortId!!");
        }
    }

    private PageCursor<ProductResponse> getProductResponsePageCursor(CursorRequest cursorRequest, List<Product> products, Long sortId) throws Exception {
        var productDtoList = products.stream()
                .map(this::toDto)
                .collect(Collectors.toList());

        if (sortId == 0L) {
            var nextKey = getNextKey(products);
            return new PageCursor<>(cursorRequest.next(nextKey), productDtoList);
        } else if (sortId == 1L) {
            var nextKey = getScoreNextKey(productDtoList);
            return new PageCursor<>(cursorRequest.next(nextKey), productDtoList);
        } else if (sortId == 2L) {
            var nextKey = getPriceAscNextKey(products);
            return new PageCursor<>(cursorRequest.next(nextKey), productDtoList);
        } else if (sortId == 3L) {
            var nextKey = getPriceDescNextKey(products);
            return new PageCursor<>(cursorRequest.next(nextKey), productDtoList);
        } else {
            throw new Exception("Wrong SortId!!");
        }
    }




    public List<ProductReadResponse> getProductsByIds(List<Long> productIds) {
        List<Product> products = productRepository.findProductsByIdIn(productIds);
        return products.stream()
                .map(this::toProductReadResponse)
                .collect(Collectors.toList());
    }

    public void validatePrincipalLike(Principal principal, List<ProductResponse> cursorBody){
        if (principal != null) {
            Optional<User> userOptional = userReadService.getUserPrincipal(principal.getName());
            if (userOptional.isPresent()) {
                User user = userOptional.get();
                updateLikeTrue(user, cursorBody);
            }
        }
    }

    public void updateLikeTrue(User user, List<ProductResponse> productResponseList) {
        productResponseList.forEach(
                productDto -> {
                    if (productLikeRepository.findByUserIdAndProductId(user.getId(), productDto.getId()).isPresent()) {
                        productDto.setLiked();
                    }
                }
        );
    }



    private Long getNextKey(List<Product> products){
        return products.stream()
                .mapToLong(Product::getId)
                .min()
                .orElse(CursorRequest.NONE_KEY_LONG);
    }
    private Double getScoreNextKey(List<ProductResponse> productResponses){
        return productResponses.stream()
                .mapToDouble(ProductResponse::getScore)
                .min()
                .orElse(CursorRequest.NONE_KEY_DOUBLE);
    }
    private Integer getPriceAscNextKey(List<Product> products){
        return products.stream()
                .mapToInt(Product::getPrice)
                .max()
                .orElse(CursorRequest.NONE_KEY_INTEGER);
    }
    private Integer getPriceDescNextKey(List<Product> products){
        return products.stream()
                .mapToInt(Product::getPrice)
                .min()
                .orElse(CursorRequest.NONE_KEY_INTEGER);
    }

    public Map<String, String> getClothesSizeAndColor(Product product) {
        Long typeId = product.getCategoryId();
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
    public ProductResponse toDto(Product product){
        return ProductResponse.builder()
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
    private int getDiscountPrice(Product product){
        if (product.isSaled()) {
            ProductSale productSale = productSaleRepository.findByProductId(product.getId());
            return (int) Math.round(product.getPrice() * productSale.getDiscountRate());
        } else {
            return 0;
        }
    }
//    private List<Product> findAllBestProducts() {
//        List<Product> allBestProducts = new ArrayList<>();
//
//        List<Product> clothes1 = findBestProducts(1L, 1L);
//        List<Product> clothes2 = findBestProducts(1L, 2L);
//        List<Product> clothes3 = findBestProducts(1L, 3L);
//        List<Product> clothes4 = findBestProducts(1L, 4L);
//        List<Product> clothes5 = findBestProducts(1L, 5L);
//
//        List<Product> props = findBestProducts(2L, 0L);
//        List<Product> goods = findBestProducts(3L, 0L);
//        List<Product> homeLivings = findBestProducts(4L, 0L);
//        List<Product> beauty = findBestProducts(5L, 0L);
//
//        allBestProducts.addAll(clothes1);
//        allBestProducts.addAll(clothes2);
//        allBestProducts.addAll(clothes3);
//        allBestProducts.addAll(clothes4);
//        allBestProducts.addAll(clothes5);
//
//        allBestProducts.addAll(props);
//        allBestProducts.addAll(goods);
//        allBestProducts.addAll(homeLivings);
//        allBestProducts.addAll(beauty);
//
//        return allBestProducts;
//    }
    private Double getProductScore(Long productId) {
        return productRepository.findProductScore(productId);
    }

}
