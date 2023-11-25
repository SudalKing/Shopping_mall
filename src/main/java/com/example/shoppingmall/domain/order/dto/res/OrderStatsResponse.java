package com.example.shoppingmall.domain.order.dto.res;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class OrderStatsResponse {
    private Integer totalOrdersCount;
    private Integer paymentPendingCount;
    private Integer shippingInCount;
    private Integer deliveredCount;
    private Integer confirmedPurchaseCount;
    private Integer exchangeReturnCount;
}
