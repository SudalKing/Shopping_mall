package com.example.shoppingmall.domain.user.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class AddressCommand {

    private String province;
    private String city;
    private String district;
    private String street;

}
