package com.example.shoppingmall.domain.product.product.service.cursor;

import com.example.shoppingmall.domain.product.product.dto.ProductResponse;
import com.example.shoppingmall.domain.product.product.entity.Product;
import com.example.shoppingmall.domain.product.product.repository.ProductRepository;
import com.example.shoppingmall.domain.product.product.service.cursor.util.ProductCursorUtilService;
import com.example.shoppingmall.global.error.exception.ErrorCode;
import com.example.shoppingmall.global.error.exception.InvalidValueException;
import com.example.shoppingmall.util.cursor.CursorRequest;
import com.example.shoppingmall.util.cursor.PageCursor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class ProductAllCursorReadService {
    private final ProductRepository productRepository;

    private final ProductCursorUtilService productCursorUtilService;

    public PageCursor<ProductResponse> getProductsByCursor(Number key, int size, Long sortId) throws Exception {
        CursorRequest cursorRequest = new CursorRequest(key, size);

        var products = findAll(cursorRequest, sortId);
        return productCursorUtilService.getProductResponsePageCursor(cursorRequest, products, sortId);
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
            throw new InvalidValueException("Wrong SortId", ErrorCode.INVALID_INPUT_VALUE);
        }
    }
}
