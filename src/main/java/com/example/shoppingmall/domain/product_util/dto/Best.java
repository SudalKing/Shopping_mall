package com.example.shoppingmall.domain.product_util.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
@Entity
public class Best {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long productId;
    private int totalSales;
    private int totalLike;
    private double score;

    public void updateTotalSales(int stock){
        totalSales += stock;
    }
    public void updateScore(double score){
        this.score = score;
    }
    public void updateTotalLike(int likeCount) {
        this.totalLike = likeCount;
    }
}
