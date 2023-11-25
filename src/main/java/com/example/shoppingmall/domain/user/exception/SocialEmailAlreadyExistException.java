package com.example.shoppingmall.domain.user.exception;

import com.example.shoppingmall.global.error.exception.ErrorCode;
import com.example.shoppingmall.global.error.exception.InvalidValueException;

public class SocialEmailAlreadyExistException extends InvalidValueException {

    public SocialEmailAlreadyExistException(String value, ErrorCode errorCode) {
        super(value, errorCode);
    }
}
