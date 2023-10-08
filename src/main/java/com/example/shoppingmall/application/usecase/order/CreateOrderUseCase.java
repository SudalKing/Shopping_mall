package com.example.shoppingmall.application.usecase.order;

import com.example.shoppingmall.domain.cart.entity.CartProduct;
import com.example.shoppingmall.domain.cart.service.CartWriteService;
import com.example.shoppingmall.domain.order.service.OrderProductWriteService;
import com.example.shoppingmall.domain.order.service.OrderWriteService;
import com.example.shoppingmall.domain.product_util.service.BestReadService;
import com.example.shoppingmall.domain.product_util.service.BestWriteService;
import com.example.shoppingmall.domain.product.service.ProductReadService;
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
    private final BestWriteService bestWriteService;
    private final BestReadService bestReadService;
    private final ProductReadService productReadService;

    // @Transactional으로 인해 삭제 쿼리가 씹힘 -> 이유 알아보기
    public void execute(Long userId){
        var user = userReadService.getUserEntity(userId);
        var cart = user.getCart();
        List<CartProduct> cartProducts = cart.getCartProducts();
        var order = orderWriteService.createOrder(userId, cart.getId());

        for (CartProduct cp: cartProducts) {
            var orderProduct = orderProductWriteService.createOrderProduct(cp, order);
            var product = productReadService.getProductEntity(orderProduct.getProductId());
            var best = bestReadService.readBestByProductId(orderProduct.getProductId());

            // 1. product 재고 변경
            // 2. best 누적 판매량 변경
            product.validateStockAndPrice();
            product.minusStock(orderProduct.getCount());
            bestWriteService.updateTotalSales(product.getId(), orderProduct.getCount());
            bestWriteService.updateScore(product.getId());

            order.setOrderProducts(orderProduct);
        }

        cartWriteService.deleteCart(user);
    }
}
