package com.example.shoppingmall.domain.follow.service;

import com.example.shoppingmall.domain.follow.dto.FollowDto;
import com.example.shoppingmall.domain.follow.entity.Follow;
import com.example.shoppingmall.domain.follow.repository.FollowRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class FollowReadService {
    private final FollowRepository followRepository;

    // 나를 팔로우한 사람을 찾는 것
    List<FollowDto> getFollowers(Long followingId){
        return followRepository.findAllByFollowingId(followingId)
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    // 내가 팔로우한 사람을 찾는 것
    List<FollowDto> getFollowings(Long followerId){
        return followRepository.findAllByFollowerId(followerId)
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public FollowDto toDto(Follow follow){
        return new FollowDto(
                follow.getId(),
                follow.getFollowerId(),
                follow.getFollowingId(),
                follow.getCreatedAt()
        );
    }

}
