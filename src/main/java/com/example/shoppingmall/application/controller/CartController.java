package com.example.shoppingmall.application.controller;

import com.example.shoppingmall.application.usecase.cart.ReadCartProductsUseCase;
import com.example.shoppingmall.domain.cart.dto.CartDto;
import com.example.shoppingmall.domain.cart.entity.Cart;
import com.example.shoppingmall.domain.cart.service.CartProductWriteService;
import com.example.shoppingmall.domain.cart.service.CartReadService;
import com.example.shoppingmall.domain.product.dto.res.ProductOrderResponse;
import com.example.shoppingmall.domain.product.entity.Product;
import com.example.shoppingmall.domain.product.service.ProductReadService;
import com.example.shoppingmall.domain.user.entity.User;
import com.example.shoppingmall.domain.user.service.UserReadService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
@RestController
@RequestMapping("/cart")
public class CartController {
    private final CartReadService cartReadService;
    private final CartProductWriteService cartProductWriteService;
    private final ProductReadService productReadService;
    private final UserReadService userReadService;
    private final ReadCartProductsUseCase readCartProductsUseCase;

    @Operation(summary = "장바구니 모든 상품 조회", description = "[인증 필요]")
    @ApiResponse(responseCode = "200", description = "OK")
    @GetMapping("/get/all")
    public ResponseEntity<List<ProductOrderResponse>> getUserAllCartProducts(Principal principal){
        return ResponseEntity.ok(readCartProductsUseCase.execute(principal));
    }

    @Operation(summary = "장바구니 상품 개수 감소", description = "[인증 필요]")
    @ApiResponse(responseCode = "200", description = "OK")
    @PutMapping("/product/minus/{productId}")
    public ResponseEntity<Void> createCartProduct(Principal principal, @PathVariable Long productId) throws Exception {
        User user = userReadService.getUserByEmail(principal.getName());
        Cart cart = cartReadService.getCartInfo(user.getId());
        Product product = productReadService.getProductEntity(productId);

        cartProductWriteService.decreaseCartProductCount(cart, product);

        return ResponseEntity.ok().build();
    }

    @Operation(summary = "장바구니 상품 개수 증가", description = "[인증 필요]")
    @ApiResponse(responseCode = "200", description = "OK")
    @PutMapping("/product/plus/{productId}")
    public ResponseEntity<Void> increaseCartProduct(Principal principal, @PathVariable Long productId) throws Exception {
        User user = userReadService.getUserByEmail(principal.getName());
        Cart cart = cartReadService.getCartInfo(user.getId());
        Product product = productReadService.getProductEntity(productId);

        cartProductWriteService.increaseCartProductCount(cart, product);

        return ResponseEntity.ok().build();
    }

    @Operation(summary = "장바구니 상품 추가", description = "[인증 필요]")
    @ApiResponse(responseCode = "200", description = "OK")
    @GetMapping("/product/{productId}")
    public ResponseEntity<Void> decreaseCartProductCount(Principal principal, @PathVariable Long productId){
        User user = userReadService.getUserByEmail(principal.getName());
        Cart cart = cartReadService.getCartInfo(user.getId());
        Product product = productReadService.getProductEntity(productId);

        cartProductWriteService.createCartProduct(cart, product);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "장바구니 상품 삭제", description = "[인증 필요]")
    @ApiResponse(responseCode = "200", description = "OK")
    @DeleteMapping("/product/{productId}")
    public ResponseEntity<Void> deleteCartProduct(Principal principal, @PathVariable Long productId){
        User user = userReadService.getUserByEmail(principal.getName());
        Cart cart = cartReadService.getCartInfo(user.getId());
        Product product = productReadService.getProductEntity(productId);

        cartProductWriteService.deleteCartProduct(cart, product);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "장바구니 상품 전체 삭제", description = "[인증 필요]")
    @ApiResponse(responseCode = "200", description = "OK")
    @DeleteMapping("/product/all")
    public ResponseEntity<Void> deleteAllCartProducts(Principal principal){
        User user = userReadService.getUserByEmail(principal.getName());
        Cart cart = cartReadService.getCartInfo(user.getId());

        cartProductWriteService.deleteAllCartProducts(cart);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "장바구니 조회", description = "[인증 필요]")
    @ApiResponse(responseCode = "200", description = "OK")
    @GetMapping("/get/info")
    public ResponseEntity<CartDto> readCart(Principal principal){
        User user = userReadService.getUserByEmail(principal.getName());
        Cart cart = cartReadService.getCartInfo(user.getId());

        return ResponseEntity.ok(cartReadService.toDto(cart));
    }
}
