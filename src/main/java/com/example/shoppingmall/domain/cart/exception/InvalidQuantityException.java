package com.example.shoppingmall.domain.cart.exception;

import com.example.shoppingmall.global.error.exception.BusinessException;
import com.example.shoppingmall.global.error.exception.ErrorCode;

public class InvalidQuantityException extends BusinessException {

    public InvalidQuantityException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }
}
