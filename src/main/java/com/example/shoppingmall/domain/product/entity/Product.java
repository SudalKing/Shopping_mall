package com.example.shoppingmall.domain.product.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Objects;

@NoArgsConstructor
@Getter
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

    @NotNull
    private String modelName;

    private Integer price;
    private Integer stock;

    @NotBlank
    private String description;

    @NotNull
    private Long categoryId;

    @NotNull
    private LocalDateTime createdAt;

    @NotNull
    private boolean deleted;

    @Version
    private Long version;

    @Builder
    public Product(Long id, String name, String modelName, Integer price, Integer stock,
                   String description, Long categoryId, LocalDateTime createdAt, boolean deleted, Long version) {
        this.id = id;
        this.name = Objects.requireNonNull(name);
        this.modelName = Objects.requireNonNull(modelName);
        this.price = price;
        this.stock = stock;
        this.description = Objects.requireNonNull(description);
        this.categoryId = Objects.requireNonNull(categoryId);
        this.createdAt = createdAt == null ? LocalDateTime.now() : createdAt;
        this.deleted = deleted;
        this.version = version == null ? 0 : version;
    }

    public void update(String newName, String newModelName, int newPrice, String newDescription){
        name = newName;
        modelName = newModelName;
        price = newPrice;
        description = newDescription;
    }

    public void addStock(int count){
        stock += count;
    }
    public void validateStockAndPrice(){
        if(stock <= 0 || price <= 0) deleted = true;
    }
}
