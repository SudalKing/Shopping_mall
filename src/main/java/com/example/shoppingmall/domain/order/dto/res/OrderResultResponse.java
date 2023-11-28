package com.example.shoppingmall.domain.order.dto.res;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class OrderResultResponse {
    private Long orderId;
    private LocalDateTime createdAt;
}
