package com.example.shoppingmall.domain.user.service;

import com.example.shoppingmall.domain.user.dto.AddressCommand;
import com.example.shoppingmall.domain.user.dto.AddressDto;
import com.example.shoppingmall.domain.user.entity.Address;
import com.example.shoppingmall.domain.user.repository.AddressRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AddressWriteService {

    private final AddressRepository addressRepository;

    public AddressDto createAddress(Long userId, AddressCommand addressCommand){
        var address = Address.builder()
                .userId(userId)
                .province(addressCommand.getProvince())
                .city(addressCommand.getCity())
                .district(addressCommand.getDistrict())
                .street(addressCommand.getStreet())
                .build();
        var savedAddress = addressRepository.save(address);
        return toDto(savedAddress);
    }

    public void deleteAddress(Long id){
        addressRepository.deleteById(id);
    }

    public AddressDto toDto(Address address){
//        return AddressDto.builder()
//                .id(address.getId())
//                .userId(address.getUserId())
//                .province(address.getProvince())
//                .city(address.getCity())
//                .district(address.getDistrict())
//                .street(address.getStreet())
//                .createdAt(address.getCreatedAt())
//                .build();
        return new AddressDto(
                address.getId(),
                address.getUserId(),
                address.getProvince(),
                address.getCity(),
                address.getDistrict(),
                address.getStreet(),
                address.getCreatedAt()
        );
    }
}
