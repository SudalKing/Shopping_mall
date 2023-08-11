package com.example.shoppingmall.domain.user.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Objects;

@NoArgsConstructor
@Getter
@Entity
@Table(uniqueConstraints =
    @UniqueConstraint(name = "USERID_UNIQUE", columnNames = "userId")
)
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private Long userId;

    @NotNull
    private String province;

    @NotNull
    private String city;

    @NotNull
    private String district;

    @NotNull
    private String street;

    @NotNull
    private LocalDateTime createdAt;

    @Builder
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