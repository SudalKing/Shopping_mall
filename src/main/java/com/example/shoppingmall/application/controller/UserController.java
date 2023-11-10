package com.example.shoppingmall.application.controller;

import com.example.shoppingmall.domain.user.dto.UserDto;
import com.example.shoppingmall.domain.user.dto.req.RegisterUserRequest;
import com.example.shoppingmall.domain.user.dto.req.UpdateUserInfoRequest;
import com.example.shoppingmall.domain.user.dto.res.UserInfoResponse;
import com.example.shoppingmall.domain.user.entity.User;
import com.example.shoppingmall.domain.user.service.UserReadService;
import com.example.shoppingmall.domain.user.service.UserWriteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.security.Principal;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/user")
public class UserController {
    private final UserReadService userReadService;
    private final UserWriteService userWriteService;

    @Operation(summary = "회원가입", description = "[인증 불필요]")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "CREATED"),
            @ApiResponse(responseCode = "400", description = "BAD_REQUEST")
    })
    @PostMapping("/signup")
    public ResponseEntity<Void> createUser(@RequestBody @Valid RegisterUserRequest registerUserRequest) {
        UserDto userDto = userWriteService.createUser(registerUserRequest);
        log.info("회원 가입 성공");

        return ResponseEntity.status(HttpStatus.CREATED)
                .location(URI.create("/user/" + userDto.getId()))
                .build();
    }

    @Operation(summary = "사용자 조회", description = "[인증 필요]")
    @ApiResponse(responseCode = "200", description = "OK")
    @GetMapping("/get/info")
    public UserInfoResponse readUser(Principal principal){
        User user = userReadService.getUserByEmail(principal.getName());
        return userReadService.findUserInfo(user);
    }

    @Operation(summary = "회원 정보 수정", description = "[인증 필요]")
    @ApiResponse(responseCode = "204", description = "OK")
    @PutMapping("/update/info")
    public ResponseEntity<Void> updateUser(Principal principal, @RequestBody UpdateUserInfoRequest updateUserInfoRequest){
        User user = userReadService.getUserByEmail(principal.getName());

        userWriteService.updateUser(user, updateUserInfoRequest);

        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "회원 탈퇴", description = "[인증 필요]")
    @ApiResponse(responseCode = "200", description = "OK")
    @DeleteMapping("/unregister")
    public void deleteUser(Principal principal){
        User user = userReadService.getUserByEmail(principal.getName());
        userWriteService.deleteUser(user);
    }

}
