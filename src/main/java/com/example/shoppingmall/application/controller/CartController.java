package com.example.shoppingmall.application.controller;

import com.example.shoppingmall.application.usecase.user.CreateUserCartProductUseCase;
import com.example.shoppingmall.application.usecase.user.ReadUserCartProductUseCase;
import com.example.shoppingmall.domain.cart.dto.CartDto;
import com.example.shoppingmall.domain.cart.dto.CartProductDto;
import com.example.shoppingmall.domain.cart.entity.Cart;
import com.example.shoppingmall.domain.cart.service.CartProductReadService;
import com.example.shoppingmall.domain.cart.service.CartProductWriteService;
import com.example.shoppingmall.domain.cart.service.CartReadService;
import com.example.shoppingmall.domain.product.entity.Product;
import com.example.shoppingmall.domain.product.service.ProductReadService;
import com.example.shoppingmall.domain.user.entity.User;
import com.example.shoppingmall.domain.user.service.UserReadService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    private final CartProductReadService cartProductReadService;
    private final CreateUserCartProductUseCase createUserCartProductUseCase;
    private final ProductReadService productReadService;
    private final ReadUserCartProductUseCase readUserCartProductUseCase;
    private final UserReadService userReadService;

    @Operation(summary = "장바구니 상품 조회", description = "장바구니 품목 조회 - [인증 필요]")
    @ApiResponse(responseCode = "200", description = "OK",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = CartProductDto.class)))
    )
    @GetMapping("/all")
    public List<CartProductDto> getUserCartProduct(Principal principal){
        User user = userReadService.getUserByEmail(principal.getName());
        return readUserCartProductUseCase.execute(user);
    }

    @Operation(summary = "장바구니 상품 개수 감소", description = "상품 id를 받아 장바구니의 상품 개수 감소 - [인증 필요]")
    @ApiResponse(responseCode = "200", description = "OK")
    @PutMapping("/product/minus/{productId}")
    public void createCartProduct(Principal principal, @PathVariable Long productId) throws Exception {
        User user = userReadService.getUserByEmail(principal.getName());
        Cart cart = cartReadService.getCartInfo(user.getId());
        Product product = productReadService.getProductEntity(productId);

        cartProductWriteService.decreaseCartProductCount(cart, product);
    }

    @Operation(summary = "장바구니 상품 개수 증가", description = "상품 id를 받아 장바구니의 상품 개수 증가 - [인증 필요]")
    @ApiResponse(responseCode = "200", description = "OK")
    @PutMapping("/product/plus/{productId}")
    public void increaseCartProduct(Principal principal, @PathVariable Long productId) throws Exception {
        User user = userReadService.getUserByEmail(principal.getName());
        Cart cart = cartReadService.getCartInfo(user.getId());
        Product product = productReadService.getProductEntity(productId);

        cartProductWriteService.increaseCartProductCount(cart, product);
    }

    @Operation(summary = "장바구니 상품 추가", description = "상품 id를 받아 장바구니에 추가 - [인증 필요]")
    @ApiResponse(responseCode = "200", description = "OK")
    @GetMapping("/product/{productId}")
    public void decreaseCartProductCount(Principal principal, @PathVariable Long productId){
        User user = userReadService.getUserByEmail(principal.getName());
        Cart cart = cartReadService.getCartInfo(user.getId());
        Product product = productReadService.getProductEntity(productId);

        cartProductWriteService.createCartProduct(cart, product);
    }

    @Operation(summary = "장바구니 상품 삭제", description = "상품 id를 받아 장바구니에 삭제", tags = {"인증 필요"})
    @ApiResponse(responseCode = "200", description = "OK")
    @DeleteMapping("/product/{productId}")
    public void deleteCartProduct(Principal principal, @PathVariable Long productId){
        User user = userReadService.getUserByEmail(principal.getName());
        Cart cart = cartReadService.getCartInfo(user.getId());
        Product product = productReadService.getProductEntity(productId);

        cartProductWriteService.deleteCartProduct(cart, product);
    }

    @Operation(summary = "장바구니 상품 전체 삭제", description = "상품 id를 받아 장바구니에 삭제", tags = {"인증 필요"})
    @ApiResponse(responseCode = "200", description = "OK")
    @DeleteMapping("/product/all")
    public void deleteAllCartProducts(Principal principal){
        User user = userReadService.getUserByEmail(principal.getName());
        Cart cart = cartReadService.getCartInfo(user.getId());

        cartProductWriteService.deleteAllCartProducts(cart);
    }

    @Operation(summary = "장바구니 조회", description = "장바구니 정보 조회", tags = {"인증 필요"})
    @ApiResponse(responseCode = "200", description = "OK")
    @GetMapping("/info")
    public CartDto readCart(Principal principal){
        User user = userReadService.getUserByEmail(principal.getName());
        Cart cart = cartReadService.getCartInfo(user.getId());

        return cartReadService.toDto(cart);
    }
}
