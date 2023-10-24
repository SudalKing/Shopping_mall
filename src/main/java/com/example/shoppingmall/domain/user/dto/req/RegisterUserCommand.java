package com.example.shoppingmall.domain.user.dto.req;

import com.example.shoppingmall.domain.user.dto.AddressInfo;
import com.example.shoppingmall.domain.user.dto.BirthDate;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Getter
@Builder
@Schema(description = "회원가입 Request class")
public class RegisterUserCommand {

    @Schema(description = "회원 이름")
    @NotNull
    private String name;

    @Email
    @Schema(description = "이메일", example = "xxx@xxx.com")
    @NotNull
    private String email;

    @Schema(description = "비밀번호")
    @NotNull
    private String password;

    @Schema(description = "비밀번호 확인")
    @NotNull
    private String confirmPassword;

    @Schema(description = "전화번호", example = "010-xxx(x)-xxxx")
    @NotNull
    private String phoneNumber;

    @Schema(description = "생년월일")
    private BirthDate birthDate;

    @Schema(description = "주소")
    private AddressInfo addressInfo;
}
