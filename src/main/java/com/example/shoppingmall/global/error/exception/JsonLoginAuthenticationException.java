package com.example.shoppingmall.global.error.exception;

import lombok.Getter;
import org.springframework.security.core.AuthenticationException;

@Getter
public class JsonLoginAuthenticationException extends AuthenticationException {

    private ErrorCode errorCode;

    public JsonLoginAuthenticationException(String message, ErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public JsonLoginAuthenticationException(ErrorCode errorCode) {super(errorCode.getCode());}
}
