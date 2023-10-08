package com.example.shoppingmall.application.controller;

import com.example.shoppingmall.application.usecase.order.CreateOrderUseCase;
import com.example.shoppingmall.domain.order.dto.OrderDto;
import com.example.shoppingmall.domain.order.service.OrderReadService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@Slf4j
@RestController
@RequestMapping("/order")
public class OrderController {
    private final CreateOrderUseCase createOrderUseCase;
    private final OrderReadService orderReadService;


    @Operation(summary = "주문 생성"
            , description = "userId를 받아 주문 생성", tags = {"USER_ROLE"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK")
    })
    @GetMapping("/{userId}")
    public void createOrder(@PathVariable Long userId){
        createOrderUseCase.execute(userId);
    }


    @Operation(summary = "주문 상세 조회"
            , description = "조회하려는 주문의 orderId와 이 주문의 장바구니 cartId를 받아 주문 상세 조회", tags = {"USER_ROLE"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(schema = @Schema(implementation = OrderDto.class)))
    })
    @GetMapping("/{cartId}/add")
    public OrderDto readOrder(@PathVariable Long cartId, @RequestParam Long orderId){
        return orderReadService.getCurrentOrder(orderId, cartId);
    }

    @GetMapping("/{userId}/all")
    public List<OrderDto> readAllOrders(@PathVariable Long userId){
        return orderReadService.getAllOrders(userId);
    }
}
