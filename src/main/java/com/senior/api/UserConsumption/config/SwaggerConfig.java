package com.senior.api.UserConsumption.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig implements WebMvcConfigurer {

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2).select()
                .apis(RequestHandlerSelectors.basePackage("com.senior.api.UserConsumption.controller")).paths(PathSelectors.any())
                .build().apiInfo(apiAppnatorInfo());
    }

    private ApiInfo apiAppnatorInfo() {
        return new ApiInfoBuilder().title("Documentation: SENIOR Test for Backend dev - User Consumption")
                .description("This project manage orders, products and order items, including the change of order price based on discount e product price").version("1.0.0")
                .contact(new Contact("Lucas Cruz Vilaça Rodrigues", "", "lucas_cruz100@live.com")).build();
    }

    @Bean
    public InternalResourceViewResolver defaultViewResolver() {
        return new InternalResourceViewResolver();
    }
}

