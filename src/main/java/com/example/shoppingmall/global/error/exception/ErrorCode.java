package com.example.shoppingmall.global.error.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;

@Getter
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum ErrorCode {
    // Common
    INVALID_INPUT_VALUE(400, "C001", "Invalid Input Value"),
    METHOD_NOT_ALLOWED(405, "C002", "Invalid Input Value"),
    ENTITY_NOT_FOUND(400, "C003", "Entity Not Found"),
    INTERNAL_SERVER_ERROR(500, "C004", "Server Error"),
    INVALID_TYPE_VALUE(400, "C005", "Invalid Type Value"),
    HANDLE_ACCESS_DENIED(403, "C006", "Access is Denied"),

    // User
    EMAIL_DUPLICATION(409, "U001", "Email is duplicated"),
    SOCIAL_EMAIL_EXIST(409, "U002", "Email has already been used in social register"),
    LOGIN_INPUT_INVALID(400, "U003", "Login input is invalid"),
    EMAIL_TYPE_SOCIAL(400, "U004", "Email is social type"),
    PASSWORD_MISMATCH(400, "U005", "Password dose not match"),
    PASSWORD_INCORRECT(400, "U006", "Password is incorrect"),
    BIRTHDATE_INPUT_INVALID(400, "U007", "Check your date of birth"),

    // Product
    OUT_OF_STOCK(400, "P001", "Out of Stock")

    // More
    ;

    private final String code;
    private final String message;
    private final int status;

    ErrorCode(final int status, final String code, final String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }
}
