package com.example.shoppingmall.domain.product.product.service.cursor.util;

import com.example.shoppingmall.domain.product.product.dto.ProductResponse;
import com.example.shoppingmall.domain.product.product.entity.Product;
import com.example.shoppingmall.domain.product.product.service.ProductReadService;
import com.example.shoppingmall.util.CursorRequest;
import com.example.shoppingmall.util.PageCursor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ProductCursorUtilService {

    private final ProductReadService productReadService;

    public PageCursor<ProductResponse> getProductResponsePageCursor(CursorRequest cursorRequest, List<Product> products, Long sortId) throws Exception {
        var productDtoList = products.stream()
                .map(productReadService::toProductResponse)
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

    public Long getNextKey(List<Product> products){
        return products.stream()
                .mapToLong(Product::getId)
                .min()
                .orElse(CursorRequest.NONE_KEY_LONG);
    }
    public Double getScoreNextKey(List<ProductResponse> productResponses){
        return productResponses.stream()
                .mapToDouble(ProductResponse::getScore)
                .min()
                .orElse(CursorRequest.NONE_KEY_DOUBLE);
    }
    public Integer getPriceAscNextKey(List<Product> products){
        return products.stream()
                .mapToInt(Product::getPrice)
                .max()
                .orElse(CursorRequest.NONE_KEY_INTEGER);
    }
    public Integer getPriceDescNextKey(List<Product> products){
        return products.stream()
                .mapToInt(Product::getPrice)
                .min()
                .orElse(CursorRequest.NONE_KEY_INTEGER);
    }

}
