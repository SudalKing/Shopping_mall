package com.example.shoppingmall.application.controller;

import com.example.shoppingmall.application.usecase.user.CreateUserFollowUseCase;
import com.example.shoppingmall.domain.follow.service.FollowReadService;
import com.example.shoppingmall.domain.follow.service.FollowWriteService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "지구를 지키는 팁 커뮤니티 팔로우 생성")
@RequiredArgsConstructor
@RestController
@RequestMapping("/follow")
public class FollowController {
    private final FollowReadService followReadService;
    private final FollowWriteService followWriteService;
    private final CreateUserFollowUseCase createUserFollowUseCase;

    @ApiOperation(value = "팔로우 생성")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "followerId", value = "팔로워 id(현재 사용자)",dataType = "Long"),
            @ApiImplicitParam(name = "followingId", value = "팔로우 하려는 대상의 id", dataType = "Long")
    })
    @Operation(description = "두 개의 사용자의 id를 받아 팔로우 관계 생성")
    @PostMapping("/{followerId}/{followingId}")
    public void createFollow(@PathVariable Long followerId, @PathVariable Long followingId){
        createUserFollowUseCase.execute(followerId, followingId);
    }
}
