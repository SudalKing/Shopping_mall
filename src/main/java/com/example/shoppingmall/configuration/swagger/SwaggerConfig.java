package com.example.shoppingmall.configuration.swagger;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
public class SwaggerConfig {

    private static final String API_TITLE = "쇼핑몰 프로젝트";
    private static final String API_DESCRIPTION = "쇼핑몰 프로젝트 API 명세서";
    private static final String API_VERSION = "v0.1";

    @Bean
    public Docket docket(){
        return new Docket(DocumentationType.OAS_30)
                .useDefaultResponseMessages(false)
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
    public GroupedOpenApi userGroupApi(){
        String path = "/user/**";
        return GroupedOpenApi.builder()
                .group("User")
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
    public GroupedOpenApi followGroupApi(){
        String path = "/follow/**";
        return GroupedOpenApi.builder()
                .group("Follow")
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
}
