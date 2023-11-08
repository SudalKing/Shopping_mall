package com.example.shoppingmall.domain.user.dto.req;

import com.example.shoppingmall.domain.user.dto.AddressInfo;
import com.example.shoppingmall.domain.user.dto.BirthDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class UpdateUserInfoRequest {
    private String name;
    private String phoneNumber;
    private String password;

    private AddressInfo addressInfo;
    private BirthDate birthDate;
}
