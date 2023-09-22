package com.example.shoppingmall.domain.follow.service;

import com.example.shoppingmall.domain.follow.entity.Follow;
import com.example.shoppingmall.domain.follow.repository.FollowRepository;
import com.example.shoppingmall.domain.user.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

@RequiredArgsConstructor
@Service
public class FollowWriteService {
    private final FollowRepository followRepository;

    public Follow createFollow(UserDto follower, UserDto following){
        Assert.isTrue(follower.getId().equals(following.getId()), "follower와 following의 아이디가 같습니다!");

        var follow = Follow.builder()
                .followingId(following.getId())
                .followerId(follower.getId())
                .build();
        return followRepository.save(follow);
    }
}
