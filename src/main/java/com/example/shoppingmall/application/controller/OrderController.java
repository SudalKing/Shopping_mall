package com.example.shoppingmall.application.controller;

import com.example.shoppingmall.application.usecase.order.CreateOrderUseCase;
import com.example.shoppingmall.domain.order.dto.res.OrderResponse;
import com.example.shoppingmall.domain.order.dto.req.OrderRequest;
import com.example.shoppingmall.domain.order.dto.res.OrderResultResponse;
import com.example.shoppingmall.domain.order.dto.res.OrderStatsResponse;
import com.example.shoppingmall.domain.order.service.OrderProductReadService;
import com.example.shoppingmall.domain.order.service.OrderReadService;
import com.example.shoppingmall.domain.user.entity.User;
import com.example.shoppingmall.domain.user.service.UserReadService;
import com.example.shoppingmall.util.cursor.PageCursor;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RequiredArgsConstructor
@Slf4j
@RestController
@RequestMapping("/order")
public class OrderController {
    private final CreateOrderUseCase createOrderUseCase;
    private final OrderReadService orderReadService;
    private final OrderProductReadService orderProductReadService;
    private final UserReadService userReadService;

    @Operation(summary = "상품 주문", description = "[인증 필요]")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "OK")})
    @PostMapping("/product")
    public ResponseEntity<OrderResultResponse> createOrderSelectProduct(Principal principal, @RequestBody OrderRequest orderRequest){
        User user = userReadService.getUserByEmail(principal.getName());

        return ResponseEntity.status(HttpStatus.OK)
                .body(createOrderUseCase.execute(user, orderRequest));
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

    @Operation(summary = "사용자 주문 현황", description = "[인증 필요]")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "OK")})
    @GetMapping("/status")
    public OrderStatsResponse readOrderStats(Principal principal)
    {
        User user = userReadService.getUserByEmail(principal.getName());
        return orderProductReadService.getOrderStats(user);
    }
}
