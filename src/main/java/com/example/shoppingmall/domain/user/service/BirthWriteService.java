package com.example.shoppingmall.domain.user.service;

import com.example.shoppingmall.domain.user.dto.BirthDate;
import com.example.shoppingmall.domain.user.entity.UserAddress;
import com.example.shoppingmall.domain.user.entity.UserBirth;
import com.example.shoppingmall.domain.user.entity.User;
import com.example.shoppingmall.domain.user.repository.BirthRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@RequiredArgsConstructor
@Service
public class BirthWriteService {
    private final BirthRepository birthRepository;

    public UserBirth createBirth(BirthDate birthDate, User user){
        UserBirth userBirth = UserBirth.builder()
                .userId(user.getId())
                .year(birthDate.getYear())
                .month(birthDate.getMonth())
                .day(birthDate.getDay())
                .build();
        return birthRepository.save(userBirth);
    }

    public void deleteBirth(User user){
        birthRepository.deleteByUserId(user.getId());
    }

    @Transactional
    public void updateBirth(User user, BirthDate birthDate) {
        UserBirth userBirth = birthRepository.findByUserId(user.getId());

        if (birthDate.getYear() != null) {
            userBirth.updateYear(birthDate.getYear());
        }

        if (birthDate.getMonth() != null) {
            userBirth.updateMonth(birthDate.getMonth());
        }

        if (birthDate.getDay() != null) {
            userBirth.updateDay(birthDate.getDay());
        }
    }
}
