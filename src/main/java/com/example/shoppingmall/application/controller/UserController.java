package com.example.shoppingmall.application.controller;

import com.example.shoppingmall.application.usecase.CreateUserCartProductUseCase;
import com.example.shoppingmall.application.usecase.ReadUserCartProductUseCase;
import com.example.shoppingmall.domain.cart.dto.CartProductDto;
import com.example.shoppingmall.domain.cart.entity.CartProduct;
import com.example.shoppingmall.domain.user.dto.AddressCommand;
import com.example.shoppingmall.domain.user.dto.AddressDto;
import com.example.shoppingmall.domain.user.dto.RegisterUserCommand;
import com.example.shoppingmall.domain.user.dto.UserDto;
import com.example.shoppingmall.domain.user.service.AddressReadService;
import com.example.shoppingmall.domain.user.service.AddressWriteService;
import com.example.shoppingmall.domain.user.service.UserReadService;
import com.example.shoppingmall.domain.user.service.UserWriteService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = {"회원 관리"})
@RequiredArgsConstructor
@RestController
@RequestMapping("/user")
public class UserController {
    private final UserReadService userReadService;
    private final UserWriteService userWriteService;
    private final AddressReadService addressReadService;
    private final AddressWriteService addressWriteService;
    private final CreateUserCartProductUseCase createUserCartProductUseCase;
    private final ReadUserCartProductUseCase readUserCartProductUseCase;

    @ApiOperation(value = "회원가입")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "email", value = "xxx@xxx.com 형식", dataType = "String"),
            @ApiImplicitParam(name = "nickname", dataType = "String"),
            @ApiImplicitParam(name = "password", dataType = "String"),
            @ApiImplicitParam(name = "phoneNumber", value = "010-xxx(x)-xxxx", dataType = "String")
    })
    @Operation(description = "nickname, email, password, phoneNumber를 받아 RegisterUserCommand class로 감싼 후 회원가입")
    @PostMapping("/signup")
    public UserDto register(RegisterUserCommand registerUserCommand){
        var user = userWriteService.createUser(registerUserCommand);
        return userReadService.toDto(user);
    }


    @ApiOperation(value = "사용자 조회")
    @ApiImplicitParam(name = "id", value = "사용자의 Id", dataType = "Long")
    @Operation(description = "id를 통해 user 조회",
            responses = @ApiResponse(responseCode = "200", description = "UserDto class 반환"
                    , content = @Content(schema = @Schema(implementation = UserDto.class)))
    )
    @GetMapping("/{id}")
    public UserDto getUser(@PathVariable Long id){
        return userReadService.getUser(id);
    }


    @ApiOperation(value = "모든 사용자 조회")
    @Operation(responses = @ApiResponse(responseCode = "200", description = "UserDto class List형 반환"
                    , content = @Content(schema = @Schema(implementation = UserDto.class)))
    )
    @GetMapping("/users")
    public List<UserDto> getAllUsers(){
        return userReadService.getAllUsers();
    }

    @ApiOperation(value = "사용자 삭제")
    @ApiImplicitParam(name = "id", value = "사용자의 Id", dataType = "Long")
    @Operation(description = "id를 통해 user 삭제", responses = @ApiResponse(responseCode = "200", description = "반환값 없음"))
    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id){
        userWriteService.deleteUser(id);
    }

//    @PostMapping("/{id}/nickname")
//    public UserDto changeNickName(@PathVariable Long id, String nickName){
//        userWriteService.changeNickName(id, nickName);
//        return userReadService.getUser(id);
//    }

    @ApiOperation(value = "주소 등록- 우편번호를 통해 주소를 제공해주는 api를 사용해 재구현 예정")
    @Operation(description = "nickname, email, password, phoneNumber를 받아 RegisterUserCommand class로 감싼 후 회원가입")
    @PostMapping("/{userId}/address")
    public AddressDto addAddress(@PathVariable Long userId, AddressCommand addressCommand){
        var address = addressWriteService.createAddress(userId, addressCommand);
        return addressReadService.toDto(address);
    }

    @ApiOperation(value = "사용자의 주소 조회")
    @ApiImplicitParam(name = "id", value = "사용자의 Id", dataType = "Long")
    @Operation(description = "id를 통해 사용자의 주소 조회",
            responses = @ApiResponse(responseCode = "200", description = "AddressDto class List형 반환"
                    , content = @Content(schema = @Schema(implementation = AddressDto.class)))
    )
    @GetMapping("/{userId}/address")
    public List<AddressDto> getUserAllAddresses(@PathVariable Long userId){
        return addressReadService.getAllAddress(userId);
    }

    @ApiOperation(value = "id를 통해 address 삭제")
    @ApiImplicitParam(name = "id", value = "사용자의 Id", dataType = "Long")
    @Operation(description = "id를 통해 주소 삭제", responses = @ApiResponse(responseCode = "200", description = "반환값 없음"))
    @DeleteMapping("/{id}/address")
    public void deleteAddress(@PathVariable Long id){
        addressWriteService.deleteAddress(id);
    }


    @GetMapping("/cart/{userId}")
    public List<CartProductDto> getUserCartProduct(@PathVariable Long userId){
        return readUserCartProductUseCase.execute(userId);
    }

    @GetMapping("/cart/{userId}/{productId}")
    public void createCart(@PathVariable Long userId, @PathVariable Long productId, @RequestParam int count){
        createUserCartProductUseCase.execute(userId, productId, count);
    }
}
