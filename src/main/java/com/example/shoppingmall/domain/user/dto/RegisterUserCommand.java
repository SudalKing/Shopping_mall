package com.example.shoppingmall.domain.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.Email;

@Getter
@Builder
@Schema(description = "회원가입 Request class")
public class RegisterUserCommand {

    @Schema(description = "회원 닉네임", defaultValue = "")
    private String nickname;

    @Email
    @Schema(description = "이메일", example = "xxx@xxx.com")
    private String email;

    @Schema(description = "비밀번호")
    private String password;

    @Schema(description = "전화번호", example = "010-xxx(x)-xxxx")
    private String phoneNumber;
}
