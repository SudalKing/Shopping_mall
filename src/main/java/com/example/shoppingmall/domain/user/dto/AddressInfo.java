package com.example.shoppingmall.domain.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class AddressInfo {
    private String postcode;
    private String address;
    private String addressDetail;
}
