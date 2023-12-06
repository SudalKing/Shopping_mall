package com.example.shoppingmall.domain.user.service;

import com.example.shoppingmall.domain.user.dto.AddressInfo;
import com.example.shoppingmall.domain.user.entity.UserAddress;
import com.example.shoppingmall.domain.user.entity.User;
import com.example.shoppingmall.domain.user.repository.AddressRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@RequiredArgsConstructor
@Service
@Transactional
public class AddressWriteService {

    private final AddressRepository addressRepository;

    public UserAddress createAddress(AddressInfo addressInfo, User user){
        UserAddress userAddress = UserAddress.builder()
                .userId(user.getId())
                .postcode(addressInfo.getPostcode())
                .address(addressInfo.getAddress())
                .addressDetail(addressInfo.getAddressDetail())
                .build();
        return addressRepository.save(userAddress);
    }

    public void deleteAddress(User user){
        addressRepository.deleteByUserId(user.getId());
    }

    public void updateAddress(User user, AddressInfo addressInfo) {
        UserAddress userAddress = addressRepository.findByUserId(user.getId());

        if (addressInfo.getPostcode() != null) {
            userAddress.updatePostCode(addressInfo.getPostcode());
        }

        if (addressInfo.getAddress() != null) {
            userAddress.updateAddress(addressInfo.getAddress());
        }

        if (addressInfo.getAddressDetail() != null) {
            userAddress.updateAddressDetail(addressInfo.getAddressDetail());
        }
    }
}
