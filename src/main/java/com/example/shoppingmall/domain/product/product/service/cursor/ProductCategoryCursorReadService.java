package com.example.shoppingmall.domain.product.product.service.cursor;

import com.example.shoppingmall.domain.product.product.dto.ProductResponse;
import com.example.shoppingmall.domain.product.product.entity.Product;
import com.example.shoppingmall.domain.product.product.repository.ProductRepository;
import com.example.shoppingmall.domain.product.product.util.ProductCursorUtilService;
import com.example.shoppingmall.util.CursorRequest;
import com.example.shoppingmall.util.PageCursor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class ProductCategoryCursorReadService {
    private final ProductRepository productRepository;

    private final ProductCursorUtilService productCursorUtilService;

    public PageCursor<ProductResponse> getProductsByCursorByCategoryAndSubCategoryId(Number key, int size, Long sortId, Long categoryId, Long subCategoryId) throws Exception {
        CursorRequest cursorRequest = new CursorRequest(key, size);

        var products = findCategoryAndSubCategoryAll(cursorRequest, sortId, categoryId, subCategoryId);
        return productCursorUtilService.getProductResponsePageCursor(cursorRequest, products, sortId);
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
}
