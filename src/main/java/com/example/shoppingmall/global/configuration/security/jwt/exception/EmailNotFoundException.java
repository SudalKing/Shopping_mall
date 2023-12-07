package com.example.shoppingmall.global.configuration.security.jwt.exception;

import com.example.shoppingmall.global.error.exception.ErrorCode;
import com.example.shoppingmall.global.error.exception.JsonLoginAuthenticationException;

public class EmailNotFoundException extends JsonLoginAuthenticationException {

    public EmailNotFoundException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }
}
