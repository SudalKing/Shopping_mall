package com.example.shoppingmall.domain.product.product.entity;

import com.example.shoppingmall.domain.cart.entity.CartProduct;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@Entity
//@Table(uniqueConstraints =
//        @UniqueConstraint(name = "MODELNAME_UNIQUE", columnNames = {"modelName"})
//)
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String name;

    private Integer price;
    private Integer stock;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private Long typeId;

    private LocalDateTime createdAt;

    @Column(nullable = false)
    private boolean saled;

    @Column(nullable = false)
    private boolean deleted;

    @Version
    private Long version;

    public void addStock(int count){
        stock += count;
    }
    public void minusStock(int count){
        stock -= count;
    }
    public void validateStockAndPrice(){
        if(stock <= 0 || price <= 0) deleted = true;
    }
    public void doSale(){
        saled = true;
    }
}
