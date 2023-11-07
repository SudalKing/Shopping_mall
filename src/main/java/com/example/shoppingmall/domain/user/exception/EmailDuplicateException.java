package com.example.shoppingmall.domain.user.exception;

import com.example.shoppingmall.global.error.exception.ErrorCode;
import com.example.shoppingmall.global.error.exception.InvalidValueException;

public class EmailDuplicateException extends InvalidValueException {

    public EmailDuplicateException(String email, ErrorCode errorCode) {
        super(email, errorCode);
    }
}
