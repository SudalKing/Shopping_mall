package com.example.shoppingmall.application.usecase;

import com.example.shoppingmall.domain.follow.service.FollowWriteService;
import com.example.shoppingmall.domain.user.service.UserReadService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CreateUserFollowUseCase {
    private final UserReadService userReadService;
    private final FollowWriteService followWriteService;

    public void execute(Long followerId, Long followingId){
        var follower = userReadService.getUser(followerId);
        var following = userReadService.getUser(followingId);

        followWriteService.createFollow(follower, following);
    }

}
