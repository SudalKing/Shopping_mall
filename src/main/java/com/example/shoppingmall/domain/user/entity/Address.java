package com.example.shoppingmall.domain.user.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;
import java.util.Objects;

@NoArgsConstructor
@Getter
@Builder
@Entity
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Long userId;
    private String province;
    private String city;
    private String district;
    private String street;
    private LocalDateTime createdAt;

    public Address(Long id, Long userId, String province, String city, String district, String street, LocalDateTime createdAt) {
        this.id = id;
        this.userId = Objects.requireNonNull(userId);
        this.province = Objects.requireNonNull(province);
        this.city = Objects.requireNonNull(city);
        this.district = Objects.requireNonNull(district);
        this.street = Objects.requireNonNull(street);
        this.createdAt = createdAt == null ? LocalDateTime.now() : createdAt;
    }



}