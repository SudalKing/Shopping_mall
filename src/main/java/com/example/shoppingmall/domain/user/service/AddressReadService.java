package com.example.shoppingmall.domain.user.service;

import com.example.shoppingmall.domain.user.repository.AddressRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AddressReadService {

    private final AddressRepository addressRepository;


}