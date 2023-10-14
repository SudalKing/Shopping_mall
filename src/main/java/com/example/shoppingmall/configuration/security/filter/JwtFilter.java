package com.example.shoppingmall.configuration.security.filter;

import com.example.shoppingmall.configuration.security.jwt.JwtUtil;
import com.example.shoppingmall.domain.user.service.CustomUserDetailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
public class JwtFilter extends OncePerRequestFilter {

    private final CustomUserDetailService customUserDetailService;
    private final String secretKey;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException
    {
        String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);
        log.info("Authorization: {}", authorization);

        // token 유효하지 않으면 차단
        if (authorization == null || !authorization.startsWith("Bearer ")){
            log.error("Authorization 이 없습니다.");
            filterChain.doFilter(request, response);
            return;
        }

        // token 꺼내기
        String token = authorization.split(" ")[1];

        // token expired 여부
        if (JwtUtil.isExpired(token, secretKey)) {
            log.error("Token이 만료되었습니다.");
            filterChain.doFilter(request, response);
        }

        // Token 에서 email 가져오기
        String email = JwtUtil.getEmail(token, secretKey);
        log.info("email: {}", email);

        // 권한 부여
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                            email,
                null,
                            List.of(new SimpleGrantedAuthority("USER")));

        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        filterChain.doFilter(request, response);
    }
}
