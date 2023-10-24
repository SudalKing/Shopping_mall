package com.example.shoppingmall.domain.user.dto.res;

import com.example.shoppingmall.domain.user.dto.AddressInfo;
import com.example.shoppingmall.domain.user.dto.BirthDate;
import com.example.shoppingmall.domain.user.util.SocialType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class UserInfoResponse {

    private Long id;
    private String name;
    private String phoneNumber;
    private String email;
    private String socialType;
    private boolean infoSet;
    private AddressInfo addressInfo;
    private BirthDate birthDate;
}
