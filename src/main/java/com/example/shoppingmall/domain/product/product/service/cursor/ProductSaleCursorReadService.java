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
public class ProductSaleCursorReadService {
    private final ProductRepository productRepository;

    private final ProductCursorUtilService productCursorUtilService;

    public PageCursor<ProductResponse> getSaleProductsByCursor(Number key, int size, Long sortId) throws Exception {
        CursorRequest cursorRequest = new CursorRequest(key, size);

        var products = findSaleAll(cursorRequest, sortId);
        return productCursorUtilService.getProductResponsePageCursor(cursorRequest, products, sortId);
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
            throw new InvalidValueException("Wrong SortId", ErrorCode.INVALID_INPUT_VALUE);
        }
    }
}
