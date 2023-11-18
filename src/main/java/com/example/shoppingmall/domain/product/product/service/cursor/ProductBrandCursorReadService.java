package com.example.shoppingmall.domain.product.product.service.cursor;

import com.example.shoppingmall.domain.product.product.dto.ProductResponse;
import com.example.shoppingmall.domain.product.product.entity.Product;
import com.example.shoppingmall.domain.product.product.repository.ProductRepository;
import com.example.shoppingmall.domain.product.product.util.ProductCursorUtilService;
import com.example.shoppingmall.global.error.exception.ErrorCode;
import com.example.shoppingmall.global.error.exception.InvalidValueException;
import com.example.shoppingmall.util.CursorRequest;
import com.example.shoppingmall.util.PageCursor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class ProductBrandCursorReadService {
    private final ProductRepository productRepository;

    private final ProductCursorUtilService productCursorUtilService;

    public PageCursor<ProductResponse> getBrandProductsByCursor(Number key, int size, Long sortId, Long brandId) throws Exception {
        CursorRequest cursorRequest = new CursorRequest(key, size);

        var products = findBrandAll(cursorRequest, sortId, brandId);
        return productCursorUtilService.getProductResponsePageCursor(cursorRequest, products, sortId);
    }

    public PageCursor<ProductResponse> getBrandCategoryProductsByCursor(Number key, int size, Long sortId, Long brandId, Long categoryId, Long subCategoryId) throws Exception {
        CursorRequest cursorRequest = new CursorRequest(key, size);

        var products = findBrandCategoryAll(cursorRequest, sortId, brandId, categoryId, subCategoryId);
        return productCursorUtilService.getProductResponsePageCursor(cursorRequest, products, sortId);
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
            throw new InvalidValueException("Wrong SortId", ErrorCode.INVALID_INPUT_VALUE);
        }
    }

    private List<Product> findBrandCategoryAll(CursorRequest cursorRequest, Long sortId, Long brandId, Long categoryId, Long subCategoryId) throws Exception {
        if (sortId == 0L) {
            if (subCategoryId == 0L) {
                if (cursorRequest.hasKey()) {
                    return productRepository.findAllByBrandCategoryOrderByIdDescHasKey(
                            cursorRequest.getKey().longValue(), brandId, categoryId, cursorRequest.getSize());
                } else {
                    return productRepository.findAllByBrandCategoryOrderByIdDescNoKey(
                            brandId, categoryId, cursorRequest.getSize());
                }
            } else {
                if (cursorRequest.hasKey()) {
                    return productRepository.findAllByBrandCategoryAndSubOrderByIdDescHasKey(
                            cursorRequest.getKey().longValue(), brandId, categoryId, subCategoryId, cursorRequest.getSize());
                } else {
                    return productRepository.findAllByBrandCategoryAndSubOrderByIdDescNoKey(
                            brandId, categoryId, subCategoryId, cursorRequest.getSize());
                }
            }
        } else if (sortId == 1L) {
            if (subCategoryId == 0L) {
                if (cursorRequest.hasKey()) {
                    return productRepository.findAllByBrandCategoryOrderByScoreHasKey(
                            cursorRequest.getKey().doubleValue(), brandId, categoryId, cursorRequest.getSize());
                } else {
                    return productRepository.findAllByBrandCategoryOrderByScoreNoKey(
                            brandId, categoryId, cursorRequest.getSize());
                }
            } else {
                if (cursorRequest.hasKey()) {
                    return productRepository.findAllByBrandCategoryAndSubOrderByScoreHasKey(
                            cursorRequest.getKey().doubleValue(), brandId, categoryId, subCategoryId, cursorRequest.getSize());
                } else {
                    return productRepository.findAllByBrandCategoryAndSubOrderByScoreNoKey(
                            brandId, categoryId, subCategoryId, cursorRequest.getSize());
                }
            }
        } else if (sortId == 2L) {
            if (subCategoryId == 0L) {
                if (cursorRequest.hasKey()) {
                    return productRepository.findAllByBrandCategoryOrderByPriceAscHasKey(
                            cursorRequest.getKey().intValue(), brandId, categoryId, cursorRequest.getSize());
                } else {
                    return productRepository.findAllByBrandCategoryOrderByPriceAscNoKey(
                            brandId, categoryId, cursorRequest.getSize());
                }
            } else {
                if (cursorRequest.hasKey()) {
                    return productRepository.findAllByBrandCategoryAndSubOrderByPriceAscHasKey(
                            cursorRequest.getKey().intValue(), brandId, categoryId, subCategoryId, cursorRequest.getSize());
                } else {
                    return productRepository.findAllByBrandCategoryAndSubOrderByPriceAscNoKey(
                            brandId, categoryId, subCategoryId, cursorRequest.getSize());
                }
            }
        } else if (sortId == 3L){
            if (subCategoryId == 0L) {
                if (cursorRequest.hasKey()) {
                    return productRepository.findAllByBrandCategoryOrderByPriceDescHasKey(
                            cursorRequest.getKey().intValue(), brandId, categoryId, cursorRequest.getSize());
                } else {
                    return productRepository.findAllByBrandCategoryOrderByPriceDescNoKey(
                            brandId, categoryId, cursorRequest.getSize());
                }
            } else {
                if (cursorRequest.hasKey()) {
                    return productRepository.findAllByBrandCategoryAndSubOrderByPriceDescHasKey(
                            cursorRequest.getKey().intValue(), brandId, categoryId, subCategoryId, cursorRequest.getSize());
                } else {
                    return productRepository.findAllByBrandCategoryAndSubOrderByPriceDescNoKey(
                            brandId, categoryId, subCategoryId, cursorRequest.getSize());
                }
            }
        } else {
            throw new Exception("Wrong SortId!!");
        }
    }
}
