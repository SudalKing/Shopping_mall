package com.example.shoppingmall.application.usecase.order;

import com.example.shoppingmall.domain.cart.entity.Cart;
import com.example.shoppingmall.domain.cart.entity.CartProduct;
import com.example.shoppingmall.domain.cart.service.CartProductReadService;
import com.example.shoppingmall.domain.cart.service.CartProductWriteService;
import com.example.shoppingmall.domain.cart.service.CartReadService;
import com.example.shoppingmall.domain.order.entity.Orders;
import com.example.shoppingmall.domain.order.service.OrderProductWriteService;
import com.example.shoppingmall.domain.order.service.OrderWriteService;
import com.example.shoppingmall.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class CreateOrderUseCase {
    private final OrderWriteService orderWriteService;
    private final OrderProductWriteService orderProductWriteService;
    private final CartReadService cartReadService;
    private final CartProductReadService cartProductReadService;
    private final CartProductWriteService cartProductWriteService;

    @Transactional
    public void execute(User user) {
        // 1. CartProduct 들을 OrderProduct 에 저장
        // 2. Order 생성
        // 3. CartProduct 삭제
        Cart cart = cartReadService.getCartInfo(user.getId());
        List<CartProduct> cartProducts = cartProductReadService.getCartProductsEntityByCart(cart);
        Orders order = orderWriteService.createOrder(user);
        order.setPriceSum(cartReadService.getTotalPrice(cart));

        for (CartProduct cartProduct: cartProducts) {
            orderProductWriteService.createOrderProduct(cartProduct, order);
        }

        cartProductWriteService.deleteAllCartProducts(cart);
    }
}
