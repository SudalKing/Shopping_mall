package com.example.shoppingmall.util.product.pagination;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class PricePageCursor<T> {
    private PriceCursorRequest priceCursorRequest;
    private List<T> body;

    public PricePageCursor(PriceCursorRequest priceCursorRequest, List<T> body) {
        this.priceCursorRequest = priceCursorRequest;
        this.body = body;
    }
}
