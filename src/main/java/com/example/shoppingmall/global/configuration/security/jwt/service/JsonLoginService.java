package com.example.shoppingmall.global.configuration.security.jwt.service;

import com.example.shoppingmall.global.configuration.security.jwt.exception.EmailNotFoundException;
import com.example.shoppingmall.global.configuration.security.jwt.exception.EmailTypeSocialException;
import com.example.shoppingmall.domain.user.repository.UserRepository;
import com.example.shoppingmall.domain.user.util.SocialType;
import com.example.shoppingmall.global.error.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Slf4j
public class JsonLoginService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws EmailNotFoundException {
        var user = userRepository.findByEmail(email)
                .orElseThrow(() -> new EmailNotFoundException(email, ErrorCode.ENTITY_NOT_FOUND));

        if (!user.getSocialType().equals(SocialType.WEB)) {
            throw new EmailTypeSocialException(email, ErrorCode.EMAIL_TYPE_SOCIAL);
        }

        return User.builder()
                .username(user.getEmail())
                .password(user.getPassword())
                .roles(user.getRole().name())
                .build();
    }
}
