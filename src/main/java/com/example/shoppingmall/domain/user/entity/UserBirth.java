package com.example.shoppingmall.domain.user.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
@Entity
public class UserBirth {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;
    private String year;
    private String month;
    private String day;

    public void updateYear(String year) {
        this.year = year;
    }

    public void updateMonth(String month) {
        this.month = month;
    }

    public void updateDay(String day) {
        this.day = day;
    }
}
