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
    private Long cartId;
    private Long orderStatusId;
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "orders", fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    private List<OrderProduct> orderProducts = new ArrayList<>();

    public void setOrderStatusId(Long orderStatusId){
        this.orderStatusId = orderStatusId;
    }
    public void setOrderProducts(OrderProduct orderProduct){
        this.orderProducts.add(orderProduct);
    }

}
