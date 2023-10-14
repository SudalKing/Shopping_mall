package com.example.shoppingmall.application.controller.security;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class SecurityController {

    @PostMapping("/review")
    public ResponseEntity<String> review(Authentication authentication){
        return ResponseEntity.ok(authentication.getName() + " 리뷰가 등록되었습니다.");
    }
}
