package com.example.shoppingmall.domain.product.product.service.cursor;

import com.example.shoppingmall.domain.product.product.dto.ProductResponse;
import com.example.shoppingmall.domain.product.product.entity.Product;
import com.example.shoppingmall.domain.product.product.repository.ProductRepository;
import com.example.shoppingmall.domain.product.product.service.cursor.util.ProductCursorUtilService;
import com.example.shoppingmall.global.error.exception.ErrorCode;
import com.example.shoppingmall.global.error.exception.InvalidValueException;
import com.example.shoppingmall.util.CursorRequest;
import com.example.shoppingmall.util.PageCursor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class ProductNewCursorReadService {
    private final ProductRepository productRepository;

    private final ProductCursorUtilService productCursorUtilService;

//    private final static long NEW_PRODUCT_DAYS = 7;
    private final static long NEW_PRODUCT_DAYS = 31;

    public PageCursor<ProductResponse> getNewProductsByCursor(Number key, int size, Long sortId) throws Exception {
        CursorRequest cursorRequest = new CursorRequest(key, size);

        var products = findNewAll(cursorRequest, sortId);
        return productCursorUtilService.getProductResponsePageCursor(cursorRequest, products, sortId);
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
            throw new InvalidValueException("Wrong SortId", ErrorCode.INVALID_INPUT_VALUE);
        }
    }

}
