package com.example.shoppingmall.domain.cart.service;

import com.example.shoppingmall.domain.cart.entity.Cart;
import com.example.shoppingmall.domain.cart.entity.CartProduct;
import com.example.shoppingmall.domain.cart.repository.CartProductRepository;
import com.example.shoppingmall.domain.product.entity.Product;
import com.example.shoppingmall.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class CartProductWriteService {
    private final CartProductRepository cartProductRepository;

    @Transactional
    public void createCartProduct(Cart cart, Product product, int count){
        var findCartProduct = cartProductRepository.findCartProductByProductIdAndCartId(product.getId(), cart.getId());

        if(findCartProduct == null){
            var cartProduct = CartProduct.builder()
                    .cart(cart)
                    .product(product)
                    .count(count)
                    .createdAt(LocalDateTime.now())
                    .build();
            cartProductRepository.save(cartProduct);
        } else {findCartProduct.addCount(count);}
    }

    @Transactional
    public void deleteAllCartProducts(User user, Cart cart){
        if(user.getId().equals(cart.getUser().getId())){
            var cartProducts = cart.getCartProducts();
            List<Long> ids = new ArrayList<>();

            for (CartProduct cartProduct: cartProducts) {
                ids.add(cartProduct.getId());
            }
            cartProductRepository.deleteAllByCartId(ids);

        } else {
            throw new RuntimeException("삭제 실패");
        }
    }
}
