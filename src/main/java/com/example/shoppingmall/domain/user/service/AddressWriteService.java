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

    @Transactional
    public void updateAddress(User user, Map<String, Object> updates) {
        UserAddress userAddress = addressRepository.findByUserId(user.getId());

        if (updates.containsKey("postcode")) {
            userAddress.updatePostCode(updates.get("postcode").toString());
        }

        if (updates.containsKey("address")) {
            userAddress.updateAddress(updates.get("address").toString());
        }

        if (updates.containsKey("addressDetail")) {
            userAddress.updateAddressDetail(updates.get("addressDetail").toString());
        }
    }
}
