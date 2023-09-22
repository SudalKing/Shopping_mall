package com.example.shoppingmall.domain.user.service;

import com.example.shoppingmall.domain.user.dto.AddressDto;
import com.example.shoppingmall.domain.user.entity.Address;
import com.example.shoppingmall.domain.user.repository.AddressRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class AddressReadService {

    private final AddressRepository addressRepository;

    public List<AddressDto> getAllAddress(Long userId){
        return addressRepository.findAllByUserId(userId)
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public AddressDto toDto(Address address){
        return AddressDto.builder()
                .id(address.getId())
                .userId(address.getUserId())
                .province(address.getProvince())
                .city(address.getCity())
                .district(address.getDistrict())
                .street(address.getStreet())
                .createdAt(address.getCreatedAt())
                .build();
    }

}
