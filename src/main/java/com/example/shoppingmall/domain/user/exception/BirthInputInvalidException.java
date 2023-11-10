package com.example.shoppingmall.domain.user.exception;

import com.example.shoppingmall.global.error.exception.ErrorCode;
import com.example.shoppingmall.global.error.exception.InvalidValueException;

public class BirthInputInvalidException extends InvalidValueException {

    public BirthInputInvalidException(String value, ErrorCode errorCode) {
        super(value, errorCode);
    }
}
