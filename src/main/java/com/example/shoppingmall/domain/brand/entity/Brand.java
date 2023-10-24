package com.example.shoppingmall.domain.brand.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
@Entity
public class Brand {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String pathName;
    private String imageUrl;
    private String logoUrl;
    private LocalDateTime createdAt;

    @PrePersist
    public void setCreatedAt(){
        createdAt = LocalDateTime.now();
    }
}
