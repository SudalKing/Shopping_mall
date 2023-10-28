package com.example.shoppingmall.domain.order.dto.req;

import com.example.shoppingmall.domain.order.dto.ProductsInfo;
import com.example.shoppingmall.domain.user.dto.AddressInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class OrderRequest {
    private String name;
    private String phoneNumber;
    private String deliveryRequest;
    private Long selectedMethod;
    private AddressInfo addressInfo;
    private List<ProductsInfo> productsInfo;
}
