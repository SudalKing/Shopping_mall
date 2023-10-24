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
//@Table(uniqueConstraints =
//    @UniqueConstraint(name = "USERID_UNIQUE", columnNames = "userId")
//)
public class UserAddress {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;
    private String postcode;
    private String address;
    private String addressDetail;


    public void updatePostCode(String postcode){
        this.postcode = postcode;
    }

    public void updateAddress(String address){
        this.address = address;
    }

    public void updateAddressDetail(String addressDetail){
        this.addressDetail = addressDetail;
    }
}