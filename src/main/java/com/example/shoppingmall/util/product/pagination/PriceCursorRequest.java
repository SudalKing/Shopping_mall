package com.example.shoppingmall.util.product.pagination;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Getter
@Builder
public class PriceCursorRequest {
    public static final Integer NONE_KEY = -1;

    private Integer key;
    private int size;

    public boolean hasKey(){
        return key != null;
    }

    public PriceCursorRequest next(Integer key){
        return new PriceCursorRequest(key, size);
    }
}
