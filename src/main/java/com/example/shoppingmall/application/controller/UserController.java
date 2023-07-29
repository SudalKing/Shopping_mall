package com.example.shoppingmall.application.controller;

import com.example.shoppingmall.domain.user.dto.AddressCommand;
import com.example.shoppingmall.domain.user.dto.AddressDto;
import com.example.shoppingmall.domain.user.dto.RegisterUserCommand;
import com.example.shoppingmall.domain.user.dto.UserDto;
import com.example.shoppingmall.domain.user.service.AddressReadService;
import com.example.shoppingmall.domain.user.service.AddressWriteService;
import com.example.shoppingmall.domain.user.service.UserReadService;
import com.example.shoppingmall.domain.user.service.UserWriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/user")
public class UserController {

    private final UserReadService userReadService;
    private final UserWriteService userWriteService;
    private final AddressReadService addressReadService;
    private final AddressWriteService addressWriteService;

    @PostMapping("/signup")
    public UserDto register(RegisterUserCommand registerUserCommand){
        var user = userWriteService.createUser(registerUserCommand);
        return userReadService.toDto(user);
    }

    @GetMapping("/{id}")
    public UserDto getUser(@PathVariable Long id){
        return userReadService.getUser(id);
    }

    @GetMapping("/users")
    public List<UserDto> getAllUsers(){
        return userReadService.getAllUsers();
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id){
        userWriteService.deleteUser(id);
    }

    @PostMapping("/{id}/email")
    public UserDto changeEmail(@PathVariable Long id, String email){
        userWriteService.changeEmail(id, email);
        return userReadService.getUser(id);
    }

    @PostMapping("/{id}/password")
    public UserDto changePassword(@PathVariable Long id, String password){
        userWriteService.changePassword(id, password);
        return userReadService.getUser(id);
    }

    @PostMapping("/{userId}/address")
    public AddressDto addAddress(@PathVariable Long userId, AddressCommand addressCommand){
        var address = addressWriteService.createAddress(userId, addressCommand);
        return addressReadService.toDto(address);
    }

    @GetMapping("/{userId}/address")
    public List<AddressDto> getUserAllAddresses(@PathVariable Long userId){
        return addressReadService.getAllAddress(userId);
    }

    @DeleteMapping("/{id}/address")
    public void deleteAddress(@PathVariable Long id){
        addressWriteService.deleteAddress(id);
    }

}
