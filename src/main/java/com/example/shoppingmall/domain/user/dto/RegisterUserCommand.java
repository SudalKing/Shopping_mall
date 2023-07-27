package com.example.shoppingmall.domain.user.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RegisterUserCommand {

    private String email;
    private String password;

}
