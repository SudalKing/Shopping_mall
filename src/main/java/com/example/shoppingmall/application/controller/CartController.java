package com.example.shoppingmall.application.controller;

import com.example.shoppingmall.application.usecase.cart.ReadCartProductsUseCase;
import com.example.shoppingmall.application.usecase.cart.CreateCartProductUseCase;
import com.example.shoppingmall.domain.cart.dto.req.CartProductRequest;
import com.example.shoppingmall.domain.cart.entity.Cart;
import com.example.shoppingmall.domain.cart.service.CartProductWriteService;
import com.example.shoppingmall.domain.cart.service.CartReadService;
import com.example.shoppingmall.domain.product.product.dto.res.ProductReadResponse;
import com.example.shoppingmall.domain.product.product.entity.Product;
import com.example.shoppingmall.domain.product.product.service.ProductReadService;
import com.example.shoppingmall.domain.user.entity.User;
import com.example.shoppingmall.domain.user.service.UserReadService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
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
    private final CreateCartProductUseCase createCartProductUseCase;

    @Operation(summary = "장바구니 모든 상품 조회", description = "[인증 필요]")
    @ApiResponse(responseCode = "200", description = "OK")
    @GetMapping("/get/all")
    public ResponseEntity<List<ProductReadResponse>> readAllCartProducts(Principal principal){
        return ResponseEntity.ok(readCartProductsUseCase.execute(principal));
    }

    @Operation(summary = "장바구니 상품 개수 감소", description = "[인증 필요]")
    @ApiResponse(responseCode = "200", description = "OK")
    @PutMapping("/product/minus/{productId}")
    public ResponseEntity<Long> decreaseCartProductAmount(Principal principal, @PathVariable Long productId) throws Exception {
        User user = userReadService.getUserByEmail(principal.getName());
        Cart cart = cartReadService.getCartInfo(user.getId());
        Product product = productReadService.getProductEntity(productId);

        cartProductWriteService.decreaseCartProductCount(cart, product);

        return ResponseEntity.status(HttpStatus.OK).body(productId);
    }

    @Operation(summary = "장바구니 상품 개수 증가", description = "[인증 필요]")
    @ApiResponse(responseCode = "200", description = "OK")
    @PutMapping("/product/plus/{productId}")
    public ResponseEntity<Long> increaseCartProductAmount(Principal principal, @PathVariable Long productId) throws Exception {
        User user = userReadService.getUserByEmail(principal.getName());
        Cart cart = cartReadService.getCartInfo(user.getId());
        Product product = productReadService.getProductEntity(productId);

        cartProductWriteService.increaseCartProductCount(cart, product);

        return ResponseEntity.status(HttpStatus.OK).body(productId);
    }

    @Operation(summary = "장바구니 상품 추가", description = "[인증 필요]")
    @ApiResponse(responseCode = "201", description = "OK")
    @PostMapping("/product/add")
    public ResponseEntity<List<ProductReadResponse>> createCartProduct(Principal principal,@RequestBody List<CartProductRequest> cartProductRequests){
        User user = userReadService.getUserByEmail(principal.getName());
        createCartProductUseCase.execute(user, cartProductRequests);

        return ResponseEntity.status(HttpStatus.CREATED).body(readCartProductsUseCase.execute(principal));
    }

    @Operation(summary = "장바구니 상품 삭제", description = "[인증 필요]")
    @ApiResponse(responseCode = "200", description = "OK")
    @DeleteMapping("/product/delete")
    public ResponseEntity<List<Long>> deleteCartProduct(Principal principal, List<Long> productIds){
        User user = userReadService.getUserByEmail(principal.getName());
        cartProductWriteService.deleteCartProduct(user, productIds);

        return ResponseEntity.status(HttpStatus.OK).body(productIds);
    }

}
