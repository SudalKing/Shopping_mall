package com.example.shoppingmall.domain.product.product_duplicated.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
@Entity
public class ProductDuplicate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long productId;

    @NotNull
    private String name;

    private Integer price;
    private Integer stock;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private Long categoryId;

    @Column(nullable = false)
    private Long subCategoryId;

    private LocalDateTime createdAt;

    @Column(nullable = false)
    private boolean saled;

    @Column(nullable = false)
    private boolean deleted;
}
