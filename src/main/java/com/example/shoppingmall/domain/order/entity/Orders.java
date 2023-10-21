package com.example.shoppingmall.domain.order.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
@Entity
public class Orders {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;
    private Long versionCount;
    private Long orderStatusId;
    private int priceSum;
    private LocalDateTime createdAt;

    public void setPriceSum(int priceSum) {
        this.priceSum = priceSum;
    }
}
