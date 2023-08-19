package com.example.shoppingmall.util;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CursorRequest {
    public static final Long NONE_KEY = -1L;

    private Long key;
    private int size;

    public boolean hasKey(){
        return key != null;
    }

    public CursorRequest next(Long key){
        return new CursorRequest(key, size);
    }
}
