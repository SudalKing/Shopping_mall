package com.example.shoppingmall.domain.user.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@Builder
@Getter
public class AddressDto {
    private Long id;
    private Long userId;
    private String province;
    private String city;
    private String district;
    private String street;
    private LocalDateTime createdAt;

    public AddressDto(Long id, Long userId, String province, String city, String district, String street, LocalDateTime createdAt) {
        this.id = id;
        this.userId = userId;
        this.province = province;
        this.city = city;
        this.district = district;
        this.street = street;
        this.createdAt = createdAt;
    }
}
