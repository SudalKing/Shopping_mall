package com.example.shoppingmall.util.cursor;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Getter
@Builder
public class CursorRequest {
    public static final Long NONE_KEY_LONG = -1L;
    public static final Double NONE_KEY_DOUBLE = -1.;
    public static final Integer NONE_KEY_INTEGER = -1;

    private Number key;
    private int size;

    public boolean hasKey(){
        return key != null;
    }

    public CursorRequest next(Number key){
        return new CursorRequest(key, size);
    }
}
