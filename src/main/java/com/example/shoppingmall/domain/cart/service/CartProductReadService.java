package com.example.shoppingmall.domain.cart.service;

import com.example.shoppingmall.domain.cart.dto.CartProductListResponse;
import com.example.shoppingmall.domain.cart.entity.Cart;
import com.example.shoppingmall.domain.cart.entity.CartProduct;
import com.example.shoppingmall.domain.cart.repository.CartProductRepository;
import com.example.shoppingmall.domain.product.service.ProductReadService;
import com.example.shoppingmall.domain.product_util.repository.ProductSaleRepository;
import com.example.shoppingmall.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class CartProductReadService {
    private final CartProductRepository cartProductRepository;


    public List<CartProduct> getCartProductsEntityByCart(Cart cart){
        return cartProductRepository.findCartProductsByCartIdAndEnabledTrue(cart.getId());
    }

    public List<Long> getProductIdsInCart(Cart cart) {
        return cartProductRepository.findProductIdsByCartId(cart.getId());
    }

}
