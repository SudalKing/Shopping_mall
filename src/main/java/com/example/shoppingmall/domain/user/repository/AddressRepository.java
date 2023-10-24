package com.example.shoppingmall.domain.user.repository;

import com.example.shoppingmall.domain.user.entity.UserAddress;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AddressRepository extends JpaRepository<UserAddress, Long> {
    List<UserAddress> findAllByUserId(Long userId);
    UserAddress findByUserId(Long userId);
    void deleteByUserId(Long userId);
}
