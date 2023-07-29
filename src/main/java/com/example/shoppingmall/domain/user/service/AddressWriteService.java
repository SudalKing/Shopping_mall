package com.example.shoppingmall.domain.user.service;

import com.example.shoppingmall.domain.user.dto.AddressCommand;
import com.example.shoppingmall.domain.user.entity.Address;
import com.example.shoppingmall.domain.user.repository.AddressRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class AddressWriteService {

    private final AddressRepository addressRepository;

    @Transactional
    public Address createAddress(Long userId, AddressCommand addressCommand){
        var address = Address.builder()
                .userId(userId)
                .province(addressCommand.getProvince())
                .city(addressCommand.getCity())
                .district(addressCommand.getDistrict())
                .street(addressCommand.getStreet())
                .build();
        return addressRepository.save(address);
    }

    public void deleteAddress(Long id){
        addressRepository.deleteById(id);
    }

}
