package com.example.shoppingmall.application.controller;

import com.example.shoppingmall.domain.user.dto.AddressCommand;
import com.example.shoppingmall.domain.user.dto.AddressDto;
import com.example.shoppingmall.domain.user.dto.RegisterUserCommand;
import com.example.shoppingmall.domain.user.dto.UserDto;
import com.example.shoppingmall.domain.user.service.AddressReadService;
import com.example.shoppingmall.domain.user.service.AddressWriteService;
import com.example.shoppingmall.domain.user.service.UserReadService;
import com.example.shoppingmall.domain.user.service.UserWriteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/user")
public class UserController {
    private final UserReadService userReadService;
    private final UserWriteService userWriteService;
    private final AddressReadService addressReadService;
    private final AddressWriteService addressWriteService;

    @Operation(summary = "회원가입", description = "RegisterUserCommand를 받아 회원 생성", tags = {"USER_ROLE"})
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "CREATED",
                    content = @Content(schema = @Schema(implementation = UserDto.class))),
            @ApiResponse(responseCode = "400", description = "BAD_REQUEST"),
            @ApiResponse(responseCode = "404", description = "NOT_FOUND"),
            @ApiResponse(responseCode = "500", description = "INTERNAL_SERVER_ERROR")
    })
    @PostMapping("/signup")
    public ResponseEntity<UserDto> register(@RequestBody RegisterUserCommand registerUserCommand){
        var userDto = userWriteService.createUser(registerUserCommand);
        log.info("회원 가입 성공");

        return ResponseEntity.created(URI.create("/user/" + userDto.getId()))
                .body(userDto);
    }

    @Operation(summary = "사용자 조회", description = "userId를 통한 사용자 조회 기능", tags = {"ADMIN_ROLE"})
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "OK",
                    content = @Content(schema = @Schema(implementation = UserDto.class))
            )
    })
    @GetMapping("/{userId}")
    public UserDto getUser(@PathVariable Long userId){
        return userReadService.getUser(userId);
    }


    @Operation(summary = "모든 사용자 조회", description = "모든 사용자 조회 기능", tags = {"ADMIN_ROLE"})
    @ApiResponse(
            responseCode = "200",
            description = "OK",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = UserDto.class)))
    )
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/users")
    public List<UserDto> getAllUsers(){
        return userReadService.getAllUsers();
    }


    @Operation(summary = "사용자 삭제", description = "userId를 통한 사용자 삭제 기능", tags = {"ADMIN_ROLE"})
    @ApiResponse(
            responseCode = "200",
            description = "OK"
    )
    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable Long userId){
        userWriteService.deleteUser(userId);
    }


    @Operation(summary = "사용자 주소 생성", description = "userId와 AddressCommand를 받아 사용자의 주소 생성", tags = {"USER_ROLE"})
    @ApiResponse(
            responseCode = "201",
            description = "OK",
            content = @Content(schema = @Schema(implementation = AddressDto.class))
    )
    @PostMapping("/{userId}/address")
    public ResponseEntity<AddressDto> addAddress(@PathVariable Long userId, AddressCommand addressCommand){
        var addressDto = addressWriteService.createAddress(userId, addressCommand);
        return ResponseEntity.created(URI.create("/" + userId + "/address/" + addressDto.getId()))
                .body(addressDto);
    }


    @Operation(summary = "사용자 주소 조회", description = "userId를 받아 사용자의 주소 List 조회", tags = {"USER_ROLE"})
    @ApiResponse(
            responseCode = "200",
            description = "OK",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = AddressDto.class)))
    )
    @GetMapping("/{userId}/address")
    public List<AddressDto> getUserAllAddresses(@PathVariable Long userId){
        return addressReadService.getAllAddress(userId);
    }


    @Operation(summary = "사용자 주소 삭제", description = "addressId를 받아 사용자의 주소 삭제", tags = {"USER_ROLE"})
    @ApiResponse(
            responseCode = "200",
            description = "OK"
    )
    @DeleteMapping("/{addressId}/address")
    public void deleteAddress(@PathVariable Long addressId){
        addressWriteService.deleteAddress(addressId);
    }
}
