package com.example.shoppingmall.domain.cart.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
@Entity
public class CartProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long cartId;
    private Long productId;
    private int count;
    private LocalDateTime createdAt;
    private boolean enabled;

    public void addCount(int count){
        this.count += count;
    }

    public void minusCount(int count){
        this.count -= count;
    }

}
