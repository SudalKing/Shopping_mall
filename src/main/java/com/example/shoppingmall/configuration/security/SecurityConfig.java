package com.example.shoppingmall.configuration.security;

import com.example.shoppingmall.configuration.security.jwt.filter.CustomUsernamePasswordAuthenticationFilter;
import com.example.shoppingmall.configuration.security.jwt.filter.JwtFilter;
import com.example.shoppingmall.configuration.security.jwt.handler.JsonLoginFailureHandler;
import com.example.shoppingmall.configuration.security.jwt.handler.JsonLoginSuccessHandler;
import com.example.shoppingmall.configuration.security.jwt.service.JsonLoginService;
import com.example.shoppingmall.configuration.security.jwt.service.JwtService;
import com.example.shoppingmall.configuration.security.oauth2.handler.OAuth2LoginFailureHandler;
import com.example.shoppingmall.configuration.security.oauth2.handler.OAuth2LoginSuccessHandler;
import com.example.shoppingmall.configuration.security.oauth2.service.CustomOAuth2UserService;
import com.example.shoppingmall.domain.user.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.LogoutFilter;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig {
    private final UserRepository userRepository;
    private final ObjectMapper objectMapper;
    private final JsonLoginService jsonLoginService;
    private final JwtService jwtService;
    private final CustomOAuth2UserService customOAuth2UserService;
    private final OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;
    private final OAuth2LoginFailureHandler oAuth2LoginFailureHandler;


    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf().disable()
                .cors().disable()
                .headers().frameOptions().disable()
                .and()
                .httpBasic().disable()
                .formLogin().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers("/", "/login/oauth2/code/**", "/user/signup").permitAll() // 다시 해보자
                .anyRequest().authenticated() // denyAll() 옵션을 주면 토큰이 있어도 막아버림
                .and()
                // OAuth2 Login
                .oauth2Login()
                .successHandler(oAuth2LoginSuccessHandler)
                .failureHandler(oAuth2LoginFailureHandler)
                .userInfoEndpoint().userService(customOAuth2UserService);
                ;

        httpSecurity.addFilterAfter(customJsonUsernamePasswordAuthenticationFilter(), LogoutFilter.class);
        httpSecurity.addFilterBefore(jwtFilter(), CustomUsernamePasswordAuthenticationFilter.class);

        return httpSecurity.build();
    }

    /**
     * AuthenticationManager 설정 후 등록
     * PasswordEncoder를 사용하는 AuthenticationProvider 지정 (PasswordEncoder는 위에서 등록한 PasswordEncoder 사용)
     * FormLogin(기존 스프링 시큐리티 로그인)과 동일하게 DaoAuthenticationProvider 사용
     * UserDetailsService는 커스텀 LoginService로 등록
     * 또한, FormLogin과 동일하게 AuthenticationManager로는 구현체인 ProviderManager 사용(return ProviderManager)
     *
     */
    @Bean
    public AuthenticationManager authenticationManager() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder());
        provider.setUserDetailsService(jsonLoginService);
        return new ProviderManager(provider);
    }

    /**
     * 로그인 성공 시 호출되는 LoginSuccessJWTProviderHandler 빈 등록
     */
    @Bean
    public JsonLoginSuccessHandler jsonLoginSuccessHandler() {
        return new JsonLoginSuccessHandler(jwtService, userRepository);
    }

    /**
     * 로그인 실패 시 호출되는 LoginFailureHandler 빈 등록
     */
    @Bean
    public JsonLoginFailureHandler jsonLoginFailureHandler() {
        return new JsonLoginFailureHandler();
    }

    /**
     * CustomJsonUsernamePasswordAuthenticationFilter 빈 등록
     * 커스텀 필터를 사용하기 위해 만든 커스텀 필터를 Bean으로 등록
     * setAuthenticationManager(authenticationManager())로 위에서 등록한 AuthenticationManager(ProviderManager) 설정
     * 로그인 성공 시 호출할 handler, 실패 시 호출할 handler로 위에서 등록한 handler 설정
     */
    @Bean
    public CustomUsernamePasswordAuthenticationFilter customJsonUsernamePasswordAuthenticationFilter() {
        CustomUsernamePasswordAuthenticationFilter customJsonUsernamePasswordLoginFilter
                = new CustomUsernamePasswordAuthenticationFilter(objectMapper);
        customJsonUsernamePasswordLoginFilter.setAuthenticationManager(authenticationManager());
        customJsonUsernamePasswordLoginFilter.setAuthenticationSuccessHandler(jsonLoginSuccessHandler());
        customJsonUsernamePasswordLoginFilter.setAuthenticationFailureHandler(jsonLoginFailureHandler());
        return customJsonUsernamePasswordLoginFilter;
    }

    @Bean
    public JwtFilter jwtFilter() {
        return new JwtFilter(jwtService, userRepository);
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer(){
        return (web) -> web.ignoring() // static resources 통과
                .requestMatchers(
                        PathRequest.toStaticResources().atCommonLocations()
                )
                .antMatchers("/swagger-ui/**")
                ;
    }

}
