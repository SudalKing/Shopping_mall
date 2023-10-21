package com.example.shoppingmall.application.controller;

import com.example.shoppingmall.application.usecase.order.CreateOrderUseCase;
import com.example.shoppingmall.domain.order.dto.OrderDto;
import com.example.shoppingmall.domain.order.service.OrderReadService;
import com.example.shoppingmall.domain.user.entity.User;
import com.example.shoppingmall.domain.user.service.UserReadService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
@RestController
@RequestMapping("/order")
public class OrderController {
    private final CreateOrderUseCase createOrderUseCase;
    private final OrderReadService orderReadService;
    private final UserReadService userReadService;


    @Operation(summary = "주문 생성", description = "주문 생성", tags = {"인증 필요"})
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "OK")})
    @GetMapping("/add")
    public void createOrder(Principal principal){
        User user = userReadService.getUserByEmail(principal.getName());
        createOrderUseCase.execute(user);
    }

    @Operation(summary = "주문 상세 조회"
            , description = "조회하려는 주문의 orderId를 받아 주문 상세 조회", tags = {"인증 필요"})
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "OK")})
    @GetMapping("/get/{orderId}")
    public OrderDto readOrder(@PathVariable Long orderId){
        return orderReadService.getCurrentOrder(orderId);
    }

    @Operation(summary = "사용자 주문 전체 조회", description = "사용자의 주문 전체 조회", tags = {"인증 필요"})
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "OK")})
    @GetMapping("/get/all")
    public List<OrderDto> readAllOrders(Principal principal){
        User user = userReadService.getUserByEmail(principal.getName());
        return orderReadService.getAllOrders(user);
    }
}
