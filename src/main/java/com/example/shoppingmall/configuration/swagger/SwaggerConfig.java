package com.example.shoppingmall.configuration.swagger;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.Arrays;
import java.util.List;

@Configuration
public class SwaggerConfig {

    private static final String API_TITLE = "쇼핑몰 프로젝트";
    private static final String API_DESCRIPTION = "쇼핑몰 프로젝트 API 명세서";
    private static final String API_VERSION = "v0.4";

    @Bean
    public Docket docket(){
        return new Docket(DocumentationType.OAS_30)
                .useDefaultResponseMessages(false)
//                .securityContexts(Arrays.asList(securityContext()))
//                .securitySchemes(Arrays.asList(apiKey()))
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build();
    }

    @Bean
    public OpenAPI openAPI(){
        return new OpenAPI()
                .info(new Info()
                        .title(API_TITLE)
                        .description(API_DESCRIPTION)
                        .version(API_VERSION)
                );
    }

    @Bean
    public GroupedOpenApi brandGroupApi(){
        String path = "/brand/**";
        return GroupedOpenApi.builder()
                .group("Brand")
                .pathsToMatch(path)
                .build();
    }

    @Bean
    public GroupedOpenApi cartGroupApi(){
        String path = "/cart/**";
        return GroupedOpenApi.builder()
                .group("Cart")
                .pathsToMatch(path)
                .build();
    }

    @Bean
    public GroupedOpenApi followGroupApi(){
        String path = "/follow/**";
        return GroupedOpenApi.builder()
                .group("Follow")
                .pathsToMatch(path)
                .build();
    }

    @Bean
    public GroupedOpenApi orderGroupApi(){
        String path = "/order/**";
        return GroupedOpenApi.builder()
                .group("Order")
                .pathsToMatch(path)
                .build();
    }

    @Bean
    public GroupedOpenApi postGroupApi(){
        String path = "/post/**";
        return GroupedOpenApi.builder()
                .group("Post")
                .pathsToMatch(path)
                .build();
    }

    @Bean
    public GroupedOpenApi productGroupApi(){
        String path = "/product/**";
        return GroupedOpenApi.builder()
                .group("Product")
                .pathsToMatch(path)
                .build();
    }

    @Bean
    public GroupedOpenApi userGroupApi(){
        String path = "/user/**";
        return GroupedOpenApi.builder()
                .group("User")
                .pathsToMatch(path)
                .build();
    }


//    private SecurityContext securityContext() {
//        return SecurityContext.builder()
//                .securityReferences(defaultAuth())
//                .build();
//    }
//
//    private List<SecurityReference> defaultAuth() {
//        AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
//        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
//        authorizationScopes[0] = authorizationScope;
//        return Arrays.asList(new SecurityReference("Authorization", authorizationScopes));
//    }
//
//    private ApiKey apiKey() {
//        return new ApiKey("Authorization", "Authorization", "header");
//    }
}
