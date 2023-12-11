package com.example.shoppingmall.domain.product.product.service;

import com.example.shoppingmall.domain.brand.service.BrandReadService;
import com.example.shoppingmall.domain.cart.service.CartProductReadService;
import com.example.shoppingmall.domain.product.product.dto.ClothesInfo;
import com.example.shoppingmall.domain.product.product.dto.ProductResponse;
import com.example.shoppingmall.domain.product.product.dto.res.ProductDetailResponse;
import com.example.shoppingmall.domain.product.product.dto.res.ProductInCartReadResponse;
import com.example.shoppingmall.domain.product.product.dto.res.ProductReadResponse;
import com.example.shoppingmall.domain.product.product.entity.Product;
import com.example.shoppingmall.domain.product.product.repository.ProductRepository;
import com.example.shoppingmall.domain.product.product_duplicated.entity.ProductDuplicate;
import com.example.shoppingmall.domain.product.product_duplicated.repository.ProductDuplicateRepository;
import com.example.shoppingmall.domain.product.product_like.service.ProductLikeReadService;
import com.example.shoppingmall.domain.user.entity.User;
import com.example.shoppingmall.domain.user.service.UserReadService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class ProductReadService {
    private final ProductRepository productRepository;
    private final ProductDuplicateRepository productDuplicateRepository;

    private final UserReadService userReadService;
    private final ProductLikeReadService productLikeReadService;
    private final BrandReadService brandReadService;
    private final CartProductReadService cartProductReadService;

    private final ProductUtilService productUtilService;


    public Product getProductEntity(final Long productId){
        Optional<Product> product = productRepository.findById(productId);

        return product.orElseGet(() -> toProduct(productDuplicateRepository.findByProductId(productId)));
    }

    public List<Product> getProductListByIds(final List<Long> productIds) {
        List<Product> products = productRepository.findProductsByIdInOrderByCreatedAtDesc(productIds);
        List<ProductDuplicate> dupleProducts = productDuplicateRepository.findProductDuplicatesByProductIdInOrderByCreatedAtDesc(productIds);

        List<Product> dupleProductsToProducts = dupleProducts.stream()
                .map(this::toProduct)
                .collect(Collectors.toList());

        products.addAll(dupleProductsToProducts);

        return products;
    }

    public ProductResponse getProductResponse(final Long productId) {
        return toProductResponse(getProductEntity(productId));
    }

    public int readHighestPrice(){
        return productRepository.findTopByOrderByPriceDesc().getPrice();
    }

    public List<ProductInCartReadResponse> getProductsInCartByIds(final List<Long> productIds) {
        List<Product> products = getProductListByIds(productIds);

        return products.stream()
                .map(this::toProductInCartReadResponse)
                .sorted(Comparator.comparing(ProductInCartReadResponse::getCreatedAt).reversed())
                .collect(Collectors.toList());
    }

    public List<ProductReadResponse> getProductsByIds(final List<Long> productIds) {
        List<Product> products = getProductListByIds(productIds);

        return products.stream()
                .map(this::toProductReadResponse)
                .collect(Collectors.toList());
    }

    public ProductDetailResponse getProductDetail(final String name) {
        List<Product> productList = new ArrayList<>();
        Product product = productRepository.findProductByName(name);
        productList.add(product);

        List<ProductDuplicate> productDuplicateList = productDuplicateRepository.findAllByName(name);

        List<Product> products = productDuplicateList.stream()
                        .map(this::toProduct)
                        .collect(Collectors.toList());

        productList.addAll(products);

        return ProductDetailResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .imageUrl(getUrl(product))
                .clothesInfoList(getClothesInfoList(productList))
                .description(product.getDescription())
                .brandInfo(brandReadService.getBrandInfo(product.getId()))
                .price(product.getPrice())
                .discountPrice(productUtilService.getDiscountPrice(product))
                .build();
    }

    private Product toProduct(final ProductDuplicate productDuplicate) {
        return Product.builder()
                .id(productDuplicate.getProductId())
                .categoryId(productDuplicate.getCategoryId())
                .subCategoryId(productDuplicate.getSubCategoryId())
                .name(productDuplicate.getName())
                .description(productDuplicate.getDescription())
                .price(productDuplicate.getPrice())
                .stock(productDuplicate.getStock())
                .saled(productDuplicate.isSaled())
                .deleted(productDuplicate.isDeleted())
                .createdAt(productDuplicate.getCreatedAt())
                .build();
    }

    public void validatePrincipalLike(final Principal principal, List<ProductResponse> cursorBody){
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
                    if (productLikeReadService.isLiked(user.getId(), productDto.getId())) {
                        productDto.setLiked();
                    }
                }
        );
    }

    public void validatePrincipalLike(Principal principal, ProductDetailResponse detailResponse){
        if (principal != null) {
            Optional<User> userOptional = userReadService.getUserPrincipal(principal.getName());
            if (userOptional.isPresent()) {
                User user = userOptional.get();
                updateLikeTrue(user, detailResponse);
            }
        }
    }

    public void updateLikeTrue(User user, ProductDetailResponse detailResponse) {
        if (productLikeReadService.isLiked(user.getId(), detailResponse.getClothesInfoList().get(0).getId())) {
            detailResponse.setLiked();
        }
    }

    public ProductInCartReadResponse toProductInCartReadResponse(final Product product) {
        Map<String, String> clothesInfo = productUtilService.getClothesInfo(product);

        return new ProductInCartReadResponse(
                product.getId(),
                product.getName(),
                productUtilService.getProductImageUrl(product.getId()),
                clothesInfo.get("color"),
                clothesInfo.get("size"),
                product.getPrice(),
                cartProductReadService.getAmount(product.getId()),
                productUtilService.getDiscountPrice(product),
                cartProductReadService.getCreatedAt(product.getId())
        );
    }

    public ProductReadResponse toProductReadResponse(final Product product) {
        Map<String, String> clothesInfo = productUtilService.getClothesInfo(product);

        return new ProductReadResponse(
                product.getId(),
                product.getName(),
                productUtilService.getProductImageUrl(product.getId()),
                clothesInfo.get("color"),
                clothesInfo.get("size")
        );
    }

    public ProductResponse toProductResponse(final Product product) {
        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .price(product.getPrice())
                .score(getProductScore(product.getId()))
                .description(product.getDescription())
                .imageUrl(getUrl(product))
                .discountPrice(productUtilService.getDiscountPrice(product))
                .isLiked(false)
                .brandInfo(brandReadService.getBrandInfo(product.getId()))
                .build();
    }


    private List<ClothesInfo> getClothesInfoList(final List<Product> productList) {
        List<ClothesInfo> clothesInfoList = new ArrayList<>();

        for (Product product: productList) {
            Map<String, String> clothes = productUtilService.getClothesInfo(product);

            ClothesInfo clothesInfo = ClothesInfo.builder()
                    .id(product.getId())
                    .color(clothes.get("color"))
                    .size(clothes.get("size"))
                    .build();

            clothesInfoList.add(clothesInfo);
        }

        return clothesInfoList;
    }

    public String getUrl(final Product product) {
        return productUtilService.getProductImageUrl(product.getId());
    }

    private Double getProductScore(final Long productId) {
        return productRepository.findProductScore(productId);
    }
}
