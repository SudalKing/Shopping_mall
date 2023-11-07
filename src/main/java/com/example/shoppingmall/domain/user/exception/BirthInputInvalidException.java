package com.example.shoppingmall.domain.user.exception;

import com.example.shoppingmall.domain.user.entity.UserBirth;
import com.example.shoppingmall.global.error.exception.InvalidValueException;

public class BirthInputInvalidException extends InvalidValueException {

    public BirthInputInvalidException(UserBirth userBirth) {
        super("");
    }
}
