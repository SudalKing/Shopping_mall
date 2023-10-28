package com.example.shoppingmall.application.controller;

import com.example.shoppingmall.application.usecase.order.CreateOrderUseCase;
import com.example.shoppingmall.domain.order.dto.OrderDto;
import com.example.shoppingmall.domain.order.dto.req.OrderRequest;
import com.example.shoppingmall.domain.order.service.OrderReadService;
import com.example.shoppingmall.domain.user.entity.User;
import com.example.shoppingmall.domain.user.service.UserReadService;
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
import java.util.List;

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

    @Operation(summary = "주문 상세 조회", description = "[인증 필요]")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "OK")})
    @GetMapping("/get/{orderId}")
    public ResponseEntity<OrderDto> readOrder(@PathVariable Long orderId){
        return ResponseEntity.ok(orderReadService.getCurrentOrder(orderId));
    }

    @Operation(summary = "사용자 주문 전체 조회", description = "인증 필요")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "OK")})
    @GetMapping("/get/all")
    public ResponseEntity<List<OrderDto>> readAllOrders(Principal principal){
        User user = userReadService.getUserByEmail(principal.getName());
        return ResponseEntity.ok(orderReadService.getAllOrders(user));
    }
}
