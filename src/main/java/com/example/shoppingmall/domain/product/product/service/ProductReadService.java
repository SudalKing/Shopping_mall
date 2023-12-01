package com.example.shoppingmall.domain.product.product.service;

import com.example.shoppingmall.domain.awsS3.service.ProductImageReadService;
import com.example.shoppingmall.domain.brand.repository.BrandRepository;
import com.example.shoppingmall.domain.cart.service.CartProductReadService;
import com.example.shoppingmall.domain.product.product.dto.ClothesInfo;
import com.example.shoppingmall.domain.product.product.dto.res.ProductInCartReadResponse;
import com.example.shoppingmall.domain.cart.repository.CartProductRepository;
import com.example.shoppingmall.domain.product.product.dto.ProductResponse;
import com.example.shoppingmall.domain.product.product.dto.res.ProductDetailResponse;
import com.example.shoppingmall.domain.product.product.dto.res.ProductReadResponse;
import com.example.shoppingmall.domain.product.product.entity.Product;
import com.example.shoppingmall.domain.product.product_like.ProductLikeRepository;
import com.example.shoppingmall.domain.product.product.repository.ProductRepository;
import com.example.shoppingmall.domain.product.product_duplicated.entity.ProductDuplicate;
import com.example.shoppingmall.domain.product.product_duplicated.repository.ProductDuplicateRepository;
import com.example.shoppingmall.domain.product_util.entity.ClothesProduct;
import com.example.shoppingmall.domain.product_util.entity.ProductSale;
import com.example.shoppingmall.domain.product_util.repository.ClothesProductRepository;
import com.example.shoppingmall.domain.product_util.repository.ProductSaleRepository;
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
    private final ProductLikeRepository productLikeRepository;
    private final ProductSaleRepository productSaleRepository;
    private final ClothesProductRepository clothesProductRepository;
    private final CartProductRepository cartProductRepository;
    private final BrandRepository brandRepository;

    private final ProductImageReadService productImageReadService;
    private final UserReadService userReadService;
    private final CartProductReadService cartProductReadService;

    public Product getProductEntity(Long productId){
        Optional<Product> product = productRepository.findById(productId);

        return product.orElseGet(() -> toProduct(productDuplicateRepository.findByProductId(productId)));
    }

    public List<Product> getProductListByIds(List<Long> productIds) {
        List<Product> products = productRepository.findProductsByIdInOrderByCreatedAtDesc(productIds);
        List<ProductDuplicate> dupleProducts = productDuplicateRepository.findProductDuplicatesByProductIdInOrderByCreatedAtDesc(productIds);

        List<Product> dupleProductsToProducts = dupleProducts.stream()
                .map(this::toProduct)
                .collect(Collectors.toList());

        products.addAll(dupleProductsToProducts);



        return products;
    }

    public ProductResponse getProductResponse(Long productId) {
        return toProductResponse(getProductEntity(productId));
    }

    public int readHighestPrice(){
        return productRepository.findTopByOrderByPriceDesc().getPrice();
    }

    public List<ProductInCartReadResponse> getProductsInCartByIds(List<Long> productIds) {
        List<Product> products = getProductListByIds(productIds);

        return products.stream()
                .map(this::toProductInCartReadResponse)
                .sorted(Comparator.comparing(ProductInCartReadResponse::getCreatedAt).reversed())
                .collect(Collectors.toList());
    }

    public List<ProductReadResponse> getProductsByIds(List<Long> productIds) {
        List<Product> products = getProductListByIds(productIds);

        return products.stream()
                .map(this::toProductReadResponse)
                .collect(Collectors.toList());
    }

    public ProductDetailResponse getProductDetail(String name) {
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
                .brandInfo(brandRepository.findBrandInfoByProductId(product.getId()))
                .price(product.getPrice())
                .discountPrice(getDiscountPrice(product))
                .build();
    }

    private Product toProduct(ProductDuplicate productDuplicate) {
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
        if (productLikeRepository.findByUserIdAndProductId(user.getId(), detailResponse.getClothesInfoList().get(0).getId()).isPresent()) {
            detailResponse.setLiked();
        }
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

    public ProductInCartReadResponse toProductInCartReadResponse(Product product) {
        Map<String, String> clothesInfo = getClothesSizeAndColor(product);
        return new ProductInCartReadResponse(
                product.getId(),
                product.getName(),
                productImageReadService.getUrl(product.getId()),
                clothesInfo.get("color"),
                clothesInfo.get("size"),
                product.getPrice(),
                cartProductRepository.findAmountByProductId(product.getId()),
                getDiscountPrice(product),
                cartProductReadService.getCreatedAt(product)
        );
    }

    public ProductReadResponse toProductReadResponse(Product product) {
        Map<String, String> clothesInfo = getClothesSizeAndColor(product);
        return new ProductReadResponse(
                product.getId(),
                product.getName(),
                productImageReadService.getUrl(product.getId()),
                clothesInfo.get("color"),
                clothesInfo.get("size")
        );
    }

    public ProductResponse toProductResponse(Product product) {
        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .price(product.getPrice())
                .score(getProductScore(product.getId()))
                .description(product.getDescription())
                .imageUrl(getUrl(product))
                .discountPrice(getDiscountPrice(product))
                .isLiked(false)
                .brandInfo(brandRepository.findBrandInfoByProductId(product.getId()))
                .build();
    }


    private List<ClothesInfo> getClothesInfoList(List<Product> productList) {
        List<ClothesInfo> clothesInfoList = new ArrayList<>();

        for (Product product: productList) {
            Map<String, String> clothes = getClothesSizeAndColor(product);

            ClothesInfo clothesInfo = ClothesInfo.builder()
                    .id(product.getId())
                    .color(clothes.get("color"))
                    .size(clothes.get("size"))
                    .build();

            clothesInfoList.add(clothesInfo);
        }

        return clothesInfoList;
    }

    public String getUrl(Product product) {
        return productImageReadService.getUrl(product.getId());
    }

    public int getDiscountPrice(Product product){
        Optional<ProductSale> productSale = productSaleRepository.findByProductId(product.getId());

        if (product.isSaled() && productSale.isPresent()) {
            return (int) Math.round(product.getPrice() * productSale.get().getDiscountRate());
        } else {
            return 0;
        }
    }

    private Double getProductScore(Long productId) {
        return productRepository.findProductScore(productId);
    }
}
