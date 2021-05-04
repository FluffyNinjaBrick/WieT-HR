package com.wiethr.app;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Collections;

@Configuration
@EnableSwagger2
public class SwaggerConfigurer {

//    http://localhost:8080/swagger-ui.html
//    http://localhost:8080/v2/api-docs

    @Bean
    public Docket swaggerConfiguration() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .paths(PathSelectors.ant("/api/*"))
                .apis(RequestHandlerSelectors.basePackage("com.wiethr"))
                .build()
                .apiInfo(createInfo());
    }

    private ApiInfo createInfo() {
        return new ApiInfo(
                "Wiet-hr",
                "Employee management application",
                "1.0",
                null,
                new Contact("Wiethr", null, null),
                null,
                null,
                Collections.emptyList()
        );
    }
}
