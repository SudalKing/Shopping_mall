package com.example.shoppingmall.application.controller;

import com.example.shoppingmall.application.usecase.user.CreateUserCartProductUseCase;
import com.example.shoppingmall.application.usecase.user.ReadUserCartProductUseCase;
import com.example.shoppingmall.domain.user.dto.AddressCommand;
import com.example.shoppingmall.domain.user.dto.AddressDto;
import com.example.shoppingmall.domain.user.dto.RegisterUserCommand;
import com.example.shoppingmall.domain.user.dto.UserDto;
import com.example.shoppingmall.domain.user.entity.Address;
import com.example.shoppingmall.domain.user.entity.User;
import com.example.shoppingmall.domain.user.service.AddressReadService;
import com.example.shoppingmall.domain.user.service.AddressWriteService;
import com.example.shoppingmall.domain.user.service.UserReadService;
import com.example.shoppingmall.domain.user.service.UserWriteService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.google.gson.Gson;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(UserController.class)
@AutoConfigureWebMvc
class UserControllerTest {

    @MockBean
    private UserReadService userReadService;

    @MockBean
    private UserWriteService userWriteService;

    @MockBean
    private AddressReadService addressReadService;

    @MockBean
    private AddressWriteService addressWriteService;

    @MockBean
    private CreateUserCartProductUseCase createUserCartProductUseCase;

    @MockBean
    private ReadUserCartProductUseCase readUserCartProductUseCase;

    @Autowired
    private MockMvc mockMvc;

    @DisplayName("1. [회원가입]")
    @Test
    @WithMockUser
    void test_1() throws Exception {
        // given
        RegisterUserCommand registerUserCommand = registerUserCommand();
        User user = user(1);
        UserDto userDto = userDto(1);

        doReturn(userDto).when(userWriteService)
                .createUser(any(RegisterUserCommand.class));

        String json = new ObjectMapper().writeValueAsString(registerUserCommand);

        // when
        ResultActions resultActions = mockMvc.perform(
                post("/user/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .with(csrf())
        )
        // then
                .andExpect(status().isCreated())
                .andExpect(jsonPath("email").value(userDto.getEmail()))
                .andExpect(jsonPath("nickname").value(userDto.getNickname()))
                .andExpect(jsonPath("password").value(userDto.getPassword()))
                .andDo(print());
    }

    @DisplayName("2. [사용자 조회]")
    @Test
    @WithMockUser
    void test_2() throws Exception{
        // given
        UserDto userDto = userDto(1);
        doReturn(userDto).when(userReadService)
                .getUser(1L);

        // when
        ResultActions resultActions = mockMvc.perform(
                get("/user/{userId}",1)
                        .with(csrf())
        )
        // then
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").value(user(1).getId()))
                .andDo(print());
    }

    @DisplayName("3. [전체 사용자 조회]")
    @Test
    @WithMockUser
    void test_3() throws Exception {
        // given
        List<UserDto> userDtoList = userDtoList();
        doReturn(userDtoList).when(userReadService)
                .getAllUsers();

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        // when
        ResultActions resultActions = mockMvc.perform(
                get("/user/users")
                        .with(csrf())
        );
        // then

        MvcResult mvcResult = resultActions.andExpect(status().isOk())
                        .andDo(print())
                        .andReturn();

        List<UserDto> responseUserDtos = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(),
                new TypeReference<List<UserDto>>() {}
        );

        assertEquals(5, responseUserDtos.size());

        UserDto firstUserDto = responseUserDtos.get(0);
        assertEquals("Test", firstUserDto.getNickname());
        assertEquals("010-1111-1111", firstUserDto.getPhoneNumber());
        assertEquals("test@test.com", firstUserDto.getEmail());
    }

    @DisplayName("4. [사용자 삭제]")
    @Test
    @WithMockUser
    void test_4() throws Exception{
        // given
        UserDto userDto = userDto(1);
        doReturn(userDto).when(userReadService)
                .getUser(1L);

        // when
        ResultActions resultActions = mockMvc.perform(
                        delete("/user/{userId}",1)
                                .with(csrf())
                )
                // then
                .andExpect(status().isOk())
                .andDo(print());
    }

    @DisplayName("5. [사용자 주소 생성]")
    @Test
    @WithMockUser
    void test_5() throws Exception {
        // given
        AddressCommand addressCommand = addressCommand();
        AddressDto addressDto = addressDto(1);
        User user = user(1);

        doReturn(addressDto).when(addressWriteService)
                .createAddress(eq(user.getId()), any(AddressCommand.class));


        String json = new ObjectMapper().writeValueAsString(addressCommand);

        // when
        ResultActions resultActions = mockMvc.perform(
                        post("/user/{userId}/address", user.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json)
                                .with(csrf())
                )
                // then
                .andExpect(status().isCreated())
                .andExpect(jsonPath("city").value(addressDto.getCity()))
                .andExpect(jsonPath("district").value(addressDto.getDistrict()))
                .andExpect(jsonPath("province").value(addressDto.getProvince()))
                .andExpect(jsonPath("street").value(addressDto.getStreet()))
                .andDo(print());
    }

    @DisplayName("6. [사용자 전체 주소 조회]")
    @Test
    @WithMockUser
    void test_6() throws Exception {
        // given
        List<AddressDto> addressDtoList = addressDtoList();
        doReturn(addressDtoList).when(addressReadService)
                .getAllAddress(1L);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        // when
        ResultActions resultActions = mockMvc.perform(
                get("/user/{userId}/address", 1)
                        .with(csrf())
        );
        // then

        MvcResult mvcResult = resultActions.andExpect(status().isOk())
                .andDo(print())
                .andReturn();

        List<AddressDto> responseAddressDtos = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(),
                new TypeReference<List<AddressDto>>() {}
        );

        assertEquals(3, responseAddressDtos.size());

        AddressDto firstAddressDto = responseAddressDtos.get(0);
        assertEquals(1L, firstAddressDto.getId());
        assertEquals(1L, firstAddressDto.getUserId());
        assertEquals("test-c", firstAddressDto.getCity());
    }


    private RegisterUserCommand registerUserCommand(){
        return RegisterUserCommand.builder()
                .nickname("Test")
                .email("test@test.com")
                .password("test")
                .phoneNumber("010-1111-1111")
                .build();
    }

    private UserDto userDto(int id){
        return UserDto.builder()
                .id(user(id).getId())
                .email(user(id).getEmail())
                .nickname(user(id).getNickname())
                .password(user(id).getPassword())
                .phoneNumber(user(id).getPhoneNumber())
                .enabled(user(id).isEnabled())
                .build();
    }

    private User user(int id){
        return User.builder()
                .id(Long.valueOf(id))
                .email(registerUserCommand().getEmail())
                .nickname(registerUserCommand().getNickname())
                .password(registerUserCommand().getPassword())
                .phoneNumber(registerUserCommand().getPhoneNumber())
                .enabled(true)
                .build();
    }

    private List<UserDto> userDtoList(){
        List<UserDto> userDtos = new ArrayList<>();

        for (int i = 1; i <= 5; i++) {
            userDtos.add(new UserDto(
                    Long.valueOf(i),
                    user(i).getNickname(),
                    user(i).getPhoneNumber(),
                    user(i).getEmail(),
                    user(i).getPassword(),
                    LocalDateTime.now(),
                    true
            ));
        }
        return userDtos;
    }

    private AddressCommand addressCommand(){
        return AddressCommand.builder()
                .city("test-c")
                .district("test-d")
                .province("test-p")
                .street("test-s")
                .build();
    }

    private AddressDto addressDto(int i){
        return new AddressDto(
                address(i).getId(),
                address(i).getUserId(),
                address(i).getProvince(),
                address(i).getCity(),
                address(i).getDistrict(),
                address(i).getStreet(),
                address(i).getCreatedAt()
        );
    }

    private Address address(int i){
        return Address.builder()
                .id(Long.valueOf(i))
                .userId(1L)
                .city(addressCommand().getCity())
                .district(addressCommand().getCity())
                .province(addressCommand().getProvince())
                .street(addressCommand().getStreet())
                .createdAt(LocalDateTime.now())
                .build();
    }

    private List<AddressDto> addressDtoList(){
        List<AddressDto> addressDtos = new ArrayList<>();

        for (int i = 1; i <= 3; i++) {
            addressDtos.add(new AddressDto(
                    Long.valueOf(i),
                    address(i).getUserId(),
                    address(i).getProvince(),
                    address(i).getCity(),
                    address(i).getDistrict(),
                    address(i).getStreet(),
                    address(i).getCreatedAt()
            ));
        }
        return addressDtos;
    }
}