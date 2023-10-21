package com.example.shoppingmall.application.controller;

import com.example.shoppingmall.application.usecase.user.CreateUserFollowUseCase;
import com.example.shoppingmall.domain.follow.service.FollowReadService;
import com.example.shoppingmall.domain.follow.service.FollowWriteService;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    @Operation(summary = "팔로우 생성", description = "followerId와 followingId를 받아 팔로우 관계 생성", tags = {"인증 필요"})
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "OK")})
    @PostMapping("/{followerId}/{followingId}")
    public void createFollow(@PathVariable Long followerId, @PathVariable Long followingId){
        createUserFollowUseCase.execute(followerId, followingId);
    }
}
