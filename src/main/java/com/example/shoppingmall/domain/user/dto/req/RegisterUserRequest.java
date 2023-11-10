package com.example.shoppingmall.domain.user.dto.req;

import com.example.shoppingmall.domain.user.dto.AddressInfo;
import com.example.shoppingmall.domain.user.dto.BirthDate;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Getter
@Builder
public class RegisterUserRequest {

    @NotNull
    private String name;

    @Email
    @NotNull
    private String email;

    @NotNull
    private String password;

    @NotNull
    private String confirmPassword;

    @NotNull
    private String phoneNumber;

    @NotNull
    private BirthDate birthDate;

    @NotNull
    private AddressInfo addressInfo;
}
