package com.example.shoppingmall.domain.user.exception;

import com.example.shoppingmall.global.error.exception.EntityNotFoundException;

public class EmailNotFoundException extends EntityNotFoundException {
    public EmailNotFoundException(String email) {
        super(email + " is not found");
    }
}
