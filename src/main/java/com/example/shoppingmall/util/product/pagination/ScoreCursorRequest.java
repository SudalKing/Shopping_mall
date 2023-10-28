package com.example.shoppingmall.util.product.pagination;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Getter
@Builder
public class ScoreCursorRequest {
    public static final Double NONE_KEY = -1.;

    private Double key;
    private int size;

    public boolean hasKey(){
        return key != null;
    }

    public ScoreCursorRequest next(Double key){
        return new ScoreCursorRequest(key, size);
    }
}
