package com.example.shoppingmall.domain.product.product.exception;

import com.example.shoppingmall.global.error.exception.BusinessException;
import com.example.shoppingmall.global.error.exception.ErrorCode;

public class OutOfStockException extends BusinessException {
    public OutOfStockException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }
}
