package com.example.shoppingmall.domain.product.service;

import com.example.shoppingmall.domain.awsS3.service.ProductImageReadService;
import com.example.shoppingmall.domain.product.dto.ProductDto;
import com.example.shoppingmall.domain.product.entity.Product;
import com.example.shoppingmall.domain.product.repository.ProductLikeRepository;
import com.example.shoppingmall.domain.product.repository.ProductRepository;
import com.example.shoppingmall.util.CursorRequest;
import com.example.shoppingmall.util.PageCursor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ProductReadService {
    private final ProductRepository productRepository;
    private final ProductLikeRepository productLikeRepository;
    private final ProductImageReadService productImageReadService;

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

    /**
     * 상품 전체 조회
     * @param cursorRequest
     * @return
     */
    public PageCursor<ProductDto> getProductsByCursor(CursorRequest cursorRequest, Long sortId){
        var products = findAllBy(cursorRequest, sortId);
        var productDtos = products.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
        var nextKey = getNextKey(products);

        return new PageCursor<>(cursorRequest.next(nextKey), productDtos);
    }

    /**
     * NEW 상품 전체 조회
     * @param cursorRequest
     * @return
     */
    public PageCursor<ProductDto> getNewProducts(CursorRequest cursorRequest, Long sortId){
        var products = findAllNewProduct(cursorRequest, sortId);
            var productsDtos = products.stream()
                    .map(this::toDto)
                    .collect(Collectors.toList());
            var nextKey = getNextKey(products);

            return new PageCursor<>(cursorRequest.next(nextKey), productsDtos);
    }

    /**
     * Best 상품 조회
     * @param cursorRequest
     * @return
     */
    public PageCursor<ProductDto> getBestProducts(CursorRequest cursorRequest){
        return null;
    }

    /**
     * Sale 적용 상품 전체 조회
     * @param cursorRequest
     * @return
     */
    public PageCursor<ProductDto> getSaleProducts(CursorRequest cursorRequest, Long sortId){
        var products = findAllSaledProduct(cursorRequest, sortId);
        var productsDtos = products.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
        var nextKey = getNextKey(products);

        return new PageCursor<>(cursorRequest.next(nextKey), productsDtos);
    }

    /**
     * 의류 상품 전체 조회
     * @param cursorRequest
     * @param categoryId
     * @return
     */
    public PageCursor<ProductDto> getClothesProducts(CursorRequest cursorRequest, Long categoryId, Long sortId){
        var products = findAllTypeProduct(cursorRequest, 1L, categoryId, sortId);
        var productsDtos = products.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
        var nextKey = getNextKey(products);

        return new PageCursor<>(cursorRequest.next(nextKey), productsDtos);
    }

    /**
     * 소품 상품 조회
     * @param cursorRequest
     * @param categoryId
     * @return
     */
    public PageCursor<ProductDto> getPropProducts(CursorRequest cursorRequest, Long categoryId, Long sortId){
        var products = findAllTypeProduct(cursorRequest, 2L, categoryId, sortId);
        var productsDtos = products.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
        var nextKey = getNextKey(products);

        return new PageCursor<>(cursorRequest.next(nextKey), productsDtos);
    }

    /**
     * 잡화 상품 조회
     * @param cursorRequest
     * @param categoryId
     * @return
     */
    public PageCursor<ProductDto> getGoodsProducts(CursorRequest cursorRequest, Long categoryId, Long sortId){
        var products = findAllTypeProduct(cursorRequest, 3L, categoryId, sortId);
        var productsDtos = products.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
        var nextKey = getNextKey(products);

        return new PageCursor<>(cursorRequest.next(nextKey), productsDtos);
    }

    /**
     * 홈리빙 상품 조회
     * @param cursorRequest
     * @return
     */
    public PageCursor<ProductDto> getHomeLivingProducts(CursorRequest cursorRequest, Long categoryId, Long sortId){
        var products = findAllTypeProduct(cursorRequest, 4L, categoryId, sortId);
        var productsDtos = products.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
        var nextKey = getNextKey(products);

        return new PageCursor<>(cursorRequest.next(nextKey), productsDtos);
    }

    /**
     * 뷰티 상품 조회
     * @param cursorRequest
     * @param categoryId
     * @return
     */
    public PageCursor<ProductDto> getBeautyProducts(CursorRequest cursorRequest, Long categoryId, Long sortId){
        var products = findAllTypeProduct(cursorRequest, 5L, categoryId, sortId);
        var productsDtos = products.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
        var nextKey = getNextKey(products);

        return new PageCursor<>(cursorRequest.next(nextKey), productsDtos);
    }

    /**
     * 브랜드별 상품 조회
     * @param cursorRequest
     * @param categoryId
     * @return
     */
    public PageCursor<ProductDto> getBrandProducts(CursorRequest cursorRequest, Long categoryId, Long sortId) {
        var products = findAllTypeProduct(cursorRequest, 6L, categoryId, sortId);
        var productsDtos = products.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
        var nextKey = getNextKey(products);

        return new PageCursor<>(cursorRequest.next(nextKey), productsDtos);
    }

// ===========================================메소드========================================================
    /**
     * 전체 상품 조회 메소드
     * @param cursorRequest:
     * @return L
     */
    private List<Product> findAllBy(CursorRequest cursorRequest, Long sortId) {
        if(sortId.equals(0L)) {
            if (cursorRequest.hasKey()) {
                return productRepository.findAllProductsByCursorHasKey(cursorRequest.getKey(), cursorRequest.getSize());
            } else {
                return productRepository.findAllProductsByCursorNoKey(cursorRequest.getSize());
            }
        } else if (sortId.equals(1L)) {
            if(cursorRequest.hasKey()) {
                return productRepository.findAllProductsByCursorHasKeyOrderByScore(cursorRequest.getKey(), cursorRequest.getSize());
            } else {
                return productRepository.findAllProductsByCursorNoKeyOrderByScore(cursorRequest.getSize());
            }
        } else if (sortId.equals(2L)) {
            if (cursorRequest.hasKey()) {
                return productRepository.findAllProductsByCursorHasKeyOrderByPriceAsc(cursorRequest.getKey(), cursorRequest.getSize());
            } else {
                return productRepository.findAllProductsByCursorNoKeyOrderByPriceAsc(cursorRequest.getSize());
            }
        } else if (sortId.equals(3L)) {
            if (cursorRequest.hasKey()) {
                return productRepository.findAllProductsByCursorHasKeyOrderByPriceDesc(cursorRequest.getKey(), cursorRequest.getSize());
            } else {
                return productRepository.findAllProductsByCursorNoKeyOrderByPriceDesc(cursorRequest.getSize());
            }
        } else {
            throw new RuntimeException("Wrong SortId!!!");
        }
    }

    /**
     * new 탭
     * @param
     */
    public List<Product> findAllNewProduct(CursorRequest cursorRequest, Long sortId){
        if (sortId.equals(0L)) {
            if (cursorRequest.hasKey()) {
                return productRepository.findAllProductsBetween7DaysByCursorHasKey(
                        cursorRequest.getKey(),
                        LocalDateTime.now().minusDays(7),
                        cursorRequest.getSize()
                );
            } else {
                return productRepository.findAllProductsBetween7DaysByCursorNoKey(
                        LocalDateTime.now().minusDays(7),
                        cursorRequest.getSize()
                );
            }
        } else if (sortId.equals(1L)) {
            if (cursorRequest.hasKey()) {
                return productRepository.findAllProductsBetween7DaysByCursorHasKeyOrderByScore(
                        cursorRequest.getKey(),
                        LocalDateTime.now().minusDays(7),
                        cursorRequest.getSize()
                );
            } else {
                return productRepository.findAllProductsBetween7DaysByCursorNoKeyOrderByScore(
                        LocalDateTime.now().minusDays(7),
                        cursorRequest.getSize()
                );
            }
        } else if (sortId.equals(2L)) {
            if (cursorRequest.hasKey()) {
                return productRepository.findAllProductsBetween7DaysByCursorHasKeyOrderByPriceAsc(
                        cursorRequest.getKey(),
                        LocalDateTime.now().minusDays(7),
                        cursorRequest.getSize()
                );
            } else {
                return productRepository.findAllProductsBetween7DaysByCursorNoKeyOrderByPriceAsc(
                        LocalDateTime.now().minusDays(7),
                        cursorRequest.getSize()
                );
            }
        } else if (sortId.equals(3L)) {
            if (cursorRequest.hasKey()) {
                return productRepository.findAllProductsBetween7DaysByCursorHasKeyOrderByPriceDesc(
                        cursorRequest.getKey(),
                        LocalDateTime.now().minusDays(7),
                        cursorRequest.getSize()
                );
            } else {
                return productRepository.findAllProductsBetween7DaysByCursorNoKeyOrderByPriceDesc(
                        LocalDateTime.now().minusDays(7),
                        cursorRequest.getSize()
                );
            }
        } else {
            throw new RuntimeException("Wrong SortId!!!");
        }
    }

    /**
     * Best 탭
     * @return
     */
    public List<Product> findAllBestProduct(CursorRequest cursorRequest){
        return null;
    }

    /**
     * Sale 탭
     * @return
     */
    public List<Product> findAllSaledProduct(CursorRequest cursorRequest, Long sortId){
        if (sortId.equals(0L)) {
            if (cursorRequest.hasKey()) {
                return productRepository.findAllSaleProductsHasKey(cursorRequest.getKey(), cursorRequest.getSize());
            } else {
                return productRepository.findAllSaleProductsNoKey(cursorRequest.getSize());
            }
        } else if (sortId.equals(1L)) {
            if (cursorRequest.hasKey()) {
                return productRepository.findAllSaleProductsHasKeyOrderByScore(cursorRequest.getKey(), cursorRequest.getSize());
            } else {
                return productRepository.findAllSaleProductsNoKeyOrderByScore(cursorRequest.getSize());
            }
        } else if (sortId.equals(2L)) {
            if (cursorRequest.hasKey()) {
                return productRepository.findAllSaleProductsHasKeyOrderByPriceAsc(cursorRequest.getKey(), cursorRequest.getSize());
            } else {
                return productRepository.findAllSaleProductsNoKeyOrderByPriceAsc(cursorRequest.getSize());
            }
        } else if (sortId.equals(3L)) {
            if (cursorRequest.hasKey()) {
                return productRepository.findAllSaleProductsHasKeyOrderByPriceDesc(cursorRequest.getKey(), cursorRequest.getSize());
            } else {
                return productRepository.findAllSaleProductsNoKeyOrderByPriceDesc(cursorRequest.getSize());
            }
        } else {
            throw new RuntimeException("Wrong SortId!!!");
        }
    }

    /**
     * 의류, 소품, 잡화, 홈리빙, 브랜드 :
     * categoryId = 0 이면 해당 타입 상품 전체 / 그 외에는 해당하는 카테고리 상품품
     *@param cursorRequest
     * @param typeId
     * @param categoryId
     * @return
     */
    public List<Product> findAllTypeProduct(CursorRequest cursorRequest, Long typeId, Long categoryId, Long sortId){
        if(sortId.equals(0L)) {
            if (categoryId.equals(0L)) {
                if (cursorRequest.hasKey()) {
                    return productRepository.
                            findAllTypeProductsHasKey(cursorRequest.getKey(), typeId, cursorRequest.getSize());
                } else {
                    return productRepository.
                            findAllTypeProductsNoKey(typeId, cursorRequest.getSize());
                }
            } else {
                if (cursorRequest.hasKey()) {
                    return productRepository.
                            findTypeCategoryProductsHasKey(
                                    cursorRequest.getKey(),
                                    typeId,
                                    categoryId,
                                    cursorRequest.getSize());
                } else {
                    return productRepository.
                            findTypeCategoryProductsNoKey(
                                    typeId,
                                    categoryId,
                                    cursorRequest.getSize());
                }
            }
        } else if (sortId.equals(1L)) {
            if (categoryId.equals(0L)) {
                if (cursorRequest.hasKey()) {
                    return productRepository.
                            findAllTypeProductsHasKeyOrderByScore(cursorRequest.getKey(), typeId, cursorRequest.getSize());
                } else {
                    return productRepository.
                            findAllTypeProductsNoKeyOrderByScore(typeId, cursorRequest.getSize());
                }
            } else {
                if (cursorRequest.hasKey()) {
                    return productRepository.
                            findTypeCategoryProductsHasKeyOrderByScore(
                                    cursorRequest.getKey(),
                                    typeId,
                                    categoryId,
                                    cursorRequest.getSize());
                } else {
                    return productRepository.
                            findTypeCategoryProductsNoKeyOrderByScore(
                                    typeId,
                                    categoryId,
                                    cursorRequest.getSize());
                }
            }
        } else if (sortId.equals(2L)) {
            if (categoryId.equals(0L)) {
                if (cursorRequest.hasKey()) {
                    return productRepository.
                            findAllTypeProductsHasKeyOrderByPriceAsc(cursorRequest.getKey(), typeId, cursorRequest.getSize());
                } else {
                    return productRepository.
                            findAllTypeProductsNoKeyOrderByPriceAsc(typeId, cursorRequest.getSize());
                }
            } else {
                if (cursorRequest.hasKey()) {
                    return productRepository.
                            findTypeCategoryProductsHasKeyOrderByPriceAsc(
                                    cursorRequest.getKey(),
                                    typeId,
                                    categoryId,
                                    cursorRequest.getSize());
                } else {
                    return productRepository.
                            findTypeCategoryProductsNoKeyOrderByPriceAsc(
                                    typeId,
                                    categoryId,
                                    cursorRequest.getSize());
                }
            }

        } else if (sortId.equals(3L)) {
            if (categoryId.equals(0L)) {
                if (cursorRequest.hasKey()) {
                    return productRepository.
                            findAllTypeProductsHasKeyOrderByPriceDesc(cursorRequest.getKey(), typeId, cursorRequest.getSize());
                } else {
                    return productRepository.
                            findAllTypeProductsNoKeyOrderByPriceDesc(typeId, cursorRequest.getSize());
                }
            } else {
                if (cursorRequest.hasKey()) {
                    return productRepository.
                            findTypeCategoryProductsHasKeyOrderByPriceDesc(
                                    cursorRequest.getKey(),
                                    typeId,
                                    categoryId,
                                    cursorRequest.getSize());
                } else {
                    return productRepository.
                            findTypeCategoryProductsNoKeyOrderByPriceDesc(
                                    typeId,
                                    categoryId,
                                    cursorRequest.getSize());
                }
            }
        } else {
            throw new RuntimeException("Wrong sortId!!!");
        }
    }



    public ProductDto toDto(Product product){
        return new ProductDto(
                product.getId(),
                product.getName(),
                product.getModelName(),
                product.getPrice(),
                product.getStock(),
                product.getDescription(),
                product.getTypeId(),
                product.getCategoryId(),
                product.isSaled(),
                product.isDeleted(),
                productLikeRepository.countAllByProductId(product.getId()),
                getUrls(product.getId())
        );
    }

    private List<String> getUrls(Long productId){
        var productImages = productImageReadService.readImages(productId);
        List<String> urls = new ArrayList<>();

        for (var productImage: productImages) {
            urls.add(productImage.getUploadFileUrl());
        }

        return urls;
    }

    public Product toEntity(ProductDto productDto){
        return Product.builder()
                .id(productDto.getId())
                .name(productDto.getName())
                .modelName(productDto.getModelName())
                .price(productDto.getPrice())
                .stock(productDto.getStock())
                .description(productDto.getDescription())
                .typeId(productDto.getTypeId())
                .categoryId(productDto.getCategoryId())
                .saled(productDto.isSaled())
                .deleted(productDto.isDeleted())
                .build();
    }

    private Long getNextKey(List<Product> products){
        return products.stream()
                .mapToLong(Product::getId)
                .min()
                .orElse(CursorRequest.NONE_KEY);
    }

}
