package com.example.shoppingmall.domain.user.service;

import com.example.shoppingmall.domain.user.dto.BirthDate;
import com.example.shoppingmall.domain.user.entity.UserAddress;
import com.example.shoppingmall.domain.user.entity.UserBirth;
import com.example.shoppingmall.domain.user.entity.User;
import com.example.shoppingmall.domain.user.exception.BirthInputInvalidException;
import com.example.shoppingmall.domain.user.repository.BirthRepository;
import com.example.shoppingmall.global.error.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@RequiredArgsConstructor
@Service
@Transactional
public class BirthWriteService {
    private final BirthRepository birthRepository;

    @Transactional
    public UserBirth createBirth(BirthDate birthDate, User user){
        // 날짜 검증
        validateBirthDate(birthDate);

        UserBirth userBirth = UserBirth.builder()
                .userId(user.getId())
                .year(birthDate.getYear())
                .month(birthDate.getMonth())
                .day(birthDate.getDay())
                .build();
        return birthRepository.save(userBirth);
    }

    @Transactional
    public UserBirth createBirthForOauth2(BirthDate birthDate, User user){
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

    private void validateBirthDate(BirthDate birthDate) {
        int year = Integer.parseInt(birthDate.getYear());
        int month = Integer.parseInt(birthDate.getMonth());
        int day = Integer.parseInt(birthDate.getDay());

        if (month > 12 || month < 1) {
            throw new BirthInputInvalidException(birthDate.getMonth(), ErrorCode.BIRTHDATE_INPUT_INVALID);
        }

        if (day < 1 || day > 31) {
            throw new BirthInputInvalidException(birthDate.getDay(), ErrorCode.BIRTHDATE_INPUT_INVALID);
        }

        // 4, 6, 9, 11월은 30일까지
        if ((month == 4 || month == 6 || month == 9 || month == 11) && day > 30) {
            throw new BirthInputInvalidException(birthDate.getDay(), ErrorCode.BIRTHDATE_INPUT_INVALID);
        }

        // 2월은 28일 또는 29일까지 (윤년 여부에 따라)
        if (month == 2) {
            boolean isLeapYear = (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0);
            int maxDay = isLeapYear ? 29 : 28;

            if (day > maxDay) {
                throw new BirthInputInvalidException(birthDate.getDay(), ErrorCode.BIRTHDATE_INPUT_INVALID);
            }
        }
    }
}
