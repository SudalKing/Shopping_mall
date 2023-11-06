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
@EnableWebSecurity(debug = true)
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
        return new CustomPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .cors()
                .and()
                .csrf().disable()
                .headers().frameOptions().disable()
                .and()
                .httpBasic().disable()
                .formLogin().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers("/", "/logout", "/login/oauth2/code/**", "/user/signup", "/login",
                        "/swagger-ui/**", "/v3/**", // /v3/api~ : swagger 리소스 url
                        "/product/get/**", "/brand/get/**", // 서비스 기능
                        "/health/check" )
                .permitAll()
                .anyRequest().authenticated() // denyAll() 옵션을 주면 토큰이 있어도 막아버림
                .and()
                // OAuth2 Login
                .oauth2Login()
                .successHandler(oAuth2LoginSuccessHandler)
                .failureHandler(oAuth2LoginFailureHandler)
                .userInfoEndpoint().userService(customOAuth2UserService);


        httpSecurity.addFilterAfter(customJsonUsernamePasswordAuthenticationFilter(), LogoutFilter.class);
        httpSecurity.addFilterBefore(jwtFilter(), CustomUsernamePasswordAuthenticationFilter.class);

        return httpSecurity.build();
    }

    @Bean
    public AuthenticationManager authenticationManager() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder());
        provider.setUserDetailsService(jsonLoginService);
        return new ProviderManager(provider);
    }

    @Bean
    public JsonLoginSuccessHandler jsonLoginSuccessHandler() {
        return new JsonLoginSuccessHandler(jwtService, userRepository);
    }

    @Bean
    public JsonLoginFailureHandler jsonLoginFailureHandler() {
        return new JsonLoginFailureHandler();
    }

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
