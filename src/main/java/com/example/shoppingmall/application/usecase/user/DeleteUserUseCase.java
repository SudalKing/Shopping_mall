package com.example.shoppingmall.application.usecase.user;

import com.example.shoppingmall.domain.brand.service.BrandWriteService;
import com.example.shoppingmall.domain.cart.service.CartWriteService;
import com.example.shoppingmall.domain.order.service.OrderProductWriteService;
import com.example.shoppingmall.domain.order.service.OrderWriteService;
import com.example.shoppingmall.domain.product.product_like.service.ProductLikeWriteService;
import com.example.shoppingmall.domain.product.review.service.ProductReviewWriteService;
import com.example.shoppingmall.domain.user.entity.User;
import com.example.shoppingmall.domain.user.service.AddressWriteService;
import com.example.shoppingmall.domain.user.service.BirthWriteService;
import com.example.shoppingmall.domain.user.service.UserWriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional
public class DeleteUserUseCase {
    private final UserWriteService userWriteService;
    private final BirthWriteService birthWriteService;
    private final AddressWriteService addressWriteService;
    private final CartWriteService cartWriteService;
    private final ProductReviewWriteService productReviewWriteService;
    private final ProductLikeWriteService productLikeWriteService;
    private final BrandWriteService brandWriteService;
    private final OrderWriteService orderWriteService;
    private final OrderProductWriteService orderProductWriteService;

    @Transactional
    public void execute(User user) {
        userWriteService.deleteUser(user);
        birthWriteService.deleteBirth(user);
        addressWriteService.deleteAddress(user);
        cartWriteService.deleteCart(user);
        productReviewWriteService.deleteAllReview(user.getId());
        productLikeWriteService.deleteProductLikeByUserId(user.getId());
        brandWriteService.deleteAllBrandLikeByUserId(user.getId());
        orderWriteService.deleteOrder(user.getId());
        orderProductWriteService.deleteOrderProduct(user.getId());
    }
}
