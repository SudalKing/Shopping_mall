package com.example.shoppingmall.global.configuration.security.jwt.exception;

import com.example.shoppingmall.global.error.exception.JsonLoginAuthenticationException;
import com.example.shoppingmall.global.error.exception.ErrorCode;

public class EmailTypeSocialException extends JsonLoginAuthenticationException {

    public EmailTypeSocialException(String value, ErrorCode errorCode) {
        super(value, errorCode);
    }
}
