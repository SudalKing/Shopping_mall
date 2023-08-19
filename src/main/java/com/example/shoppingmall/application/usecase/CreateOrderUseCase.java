package com.example.shoppingmall.application.usecase;

import com.example.shoppingmall.domain.cart.entity.CartProduct;
import com.example.shoppingmall.domain.cart.service.CartWriteService;
import com.example.shoppingmall.domain.order.service.OrderProductWriteService;
import com.example.shoppingmall.domain.order.service.OrderWriteService;
import com.example.shoppingmall.domain.user.service.UserReadService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class CreateOrderUseCase {
    private final CartWriteService cartWriteService;
    private final OrderWriteService orderWriteService;
    private final OrderProductWriteService orderProductWriteService;
    private final UserReadService userReadService;

    // @Transactional으로 인해 삭제 쿼리가 씹힘 -> 이유 알아보기
    public void execute(Long userId){
        var user = userReadService.getUserEntity(userId);
        var cart = user.getCart();
        List<CartProduct> cartProducts = cart.getCartProducts();
        var order = orderWriteService.createOrder(userId, cart.getId());

        for (CartProduct cp: cartProducts) {
            var orderProduct = orderProductWriteService.createOrderProduct(cp, order);
            order.setOrderProducts(orderProduct);
        }

        cartWriteService.deleteCart(user);
    }
}
