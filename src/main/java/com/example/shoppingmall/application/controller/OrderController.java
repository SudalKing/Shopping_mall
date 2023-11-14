package com.example.shoppingmall.application.controller;

import com.example.shoppingmall.application.usecase.order.CreateOrderUseCase;
import com.example.shoppingmall.domain.order.dto.res.OrderResponse;
import com.example.shoppingmall.domain.order.dto.req.OrderRequest;
import com.example.shoppingmall.domain.order.service.OrderReadService;
import com.example.shoppingmall.domain.user.entity.User;
import com.example.shoppingmall.domain.user.service.UserReadService;
import com.example.shoppingmall.util.PageCursor;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.security.Principal;

@RequiredArgsConstructor
@Slf4j
@RestController
@RequestMapping("/order")
public class OrderController {
    private final CreateOrderUseCase createOrderUseCase;
    private final OrderReadService orderReadService;
    private final UserReadService userReadService;

    @Operation(summary = "상품 주문", description = "[인증 필요]")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "OK")})
    @PostMapping("/product")
    public ResponseEntity<Void> createOrderSelectProduct(Principal principal, @RequestBody OrderRequest orderRequest){
        User user = userReadService.getUserByEmail(principal.getName());
        Long orderId = createOrderUseCase.execute(user, orderRequest);

        return ResponseEntity.status(HttpStatus.CREATED)
                .location(URI.create("/order/get/" + orderId))
                .build();
    }

    @Operation(summary = "사용자 주문 전체 조회", description = "[인증 필요]")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "OK")})
    @GetMapping("/get/all")
    public PageCursor<OrderResponse> readAllOrders(Principal principal,
                                                   @RequestParam(required = false) Number key,
                                                   int size)
    {
        User user = userReadService.getUserByEmail(principal.getName());
        return orderReadService.getAllOrdersByCursor(key, size, user);
    }
}
