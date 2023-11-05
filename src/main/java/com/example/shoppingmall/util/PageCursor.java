package com.example.shoppingmall.util;

import com.example.shoppingmall.domain.product.product.dto.ProductResponse;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class PageCursor<T> {
    private CursorRequest cursorRequest;
    private List<T> body;

    public PageCursor(CursorRequest cursorRequest, List<T> body) {
        this.cursorRequest = cursorRequest;
        this.body = body;
    }

}
