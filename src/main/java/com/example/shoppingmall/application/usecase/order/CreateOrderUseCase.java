package com.example.shoppingmall.application.usecase.order;

import com.example.shoppingmall.domain.cart.entity.Cart;
import com.example.shoppingmall.domain.cart.entity.CartProduct;
import com.example.shoppingmall.domain.cart.service.CartProductReadService;
import com.example.shoppingmall.domain.cart.service.CartProductWriteService;
import com.example.shoppingmall.domain.cart.service.CartReadService;
import com.example.shoppingmall.domain.delivery.entity.Delivery;
import com.example.shoppingmall.domain.delivery.service.DeliveryWriteService;
import com.example.shoppingmall.domain.order.dto.ProductsInfo;
import com.example.shoppingmall.domain.order.dto.req.OrderRequest;
import com.example.shoppingmall.domain.order.entity.Orders;
import com.example.shoppingmall.domain.order.service.OrderProductReadService;
import com.example.shoppingmall.domain.order.service.OrderProductWriteService;
import com.example.shoppingmall.domain.order.service.OrderWriteService;
import com.example.shoppingmall.domain.product.product.service.ProductReadService;
import com.example.shoppingmall.domain.user.dto.AddressInfo;
import com.example.shoppingmall.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class CreateOrderUseCase {
    private final OrderWriteService orderWriteService;
    private final OrderProductReadService orderProductReadService;
    private final OrderProductWriteService orderProductWriteService;
    private final ProductReadService productReadService;
    private final CartReadService cartReadService;
    private final CartProductReadService cartProductReadService;
    private final CartProductWriteService cartProductWriteService;
    private final DeliveryWriteService deliveryWriteService;

    @Transactional
    public Long execute(User user, OrderRequest orderRequest) {
        AddressInfo addressInfo = orderRequest.getAddressInfo();
        List<ProductsInfo> productsInfo = orderRequest.getProductsInfo();
        Orders order = orderWriteService.createOrder(user);
        Cart cart = cartReadService.getCartInfo(user.getId());
        List<CartProduct> cartProducts = cartProductReadService.getCartProductsEntityByCart(cart);

        Delivery delivery = Delivery.builder()
                .userName(orderRequest.getName())
                .phoneNumber(orderRequest.getPhoneNumber())
                .status("배송 준비 중")
                .deliveryRequest(orderRequest.getDeliveryRequest())
                .postcode(addressInfo.getPostcode())
                .address(addressInfo.getAddress())
                .addressDetail(addressInfo.getPostcode())
                .build();
        deliveryWriteService.createDelivery(delivery);

        List<Long> matchingProductIds = cartProducts.stream()
                .map(CartProduct::getProductId)
                .filter(productId -> productsInfo.stream()
                        .anyMatch(productInfo -> productInfo.getId().equals(productId)))
                .collect(Collectors.toList());

        for (ProductsInfo productInfo : productsInfo) {
            orderProductWriteService.createOrderProduct(productInfo, order);
        }

        if (!matchingProductIds.isEmpty()) {
//            for (Long matchingProductId: matchingProductIds) {
//                cartProductWriteService.deleteCartProduct(cart,
//                        productReadService.getProductEntity(matchingProductId)
//                );
//            }
            cartProductWriteService.deleteCartProduct(user, matchingProductIds);
        }

        orderWriteService.updateOrderPriceSum(order, orderProductReadService.getPriceSum(order));

        return order.getId();
    }

}
