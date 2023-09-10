package com.example.shoppingmall.configuration.security;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class RequestInfo {
    private String remoteIp;
    private String sessionId;
    private LocalDateTime loginTime;
}
