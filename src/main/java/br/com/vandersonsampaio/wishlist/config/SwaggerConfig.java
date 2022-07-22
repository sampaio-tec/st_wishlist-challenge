package br.com.vandersonsampaio.wishlist.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.http.HttpMethod;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.ResponseBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Response;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

    private static final String UNMAPPED_EXCEPTION = "Unmapped exception";

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("br.com.vandersonsampaio.wishlist.controller"))
                .paths(PathSelectors.any())
                .build()
                .apiInfo(metaInfo())
                .useDefaultResponseMessages(false)
                .globalResponses(HttpMethod.GET, buildGetResponse())
                .globalResponses(HttpMethod.POST, buildPostResponse())
                .globalResponses(HttpMethod.DELETE, buildDeleteResponses());
    }

    private List<Response> buildGetResponse() {
        return List.of(
                new ResponseBuilder().code("204").description("Wishlist not found.").build(),
                new ResponseBuilder().code("500").description(UNMAPPED_EXCEPTION).build()
        );
    }

    private List<Response> buildPostResponse() {
        return Arrays.asList(
                new ResponseBuilder().code("201").description("Item created successfully.").build(),
                new ResponseBuilder().code("500").description(UNMAPPED_EXCEPTION).build(),
                new ResponseBuilder().code("400").description("Some parameter was not informed correctly.").build()
        );
    }

    private List<Response> buildDeleteResponses() {
        return Arrays.asList(
                new ResponseBuilder().code("204").description("Item removed successfully.").build(),
                new ResponseBuilder().code("500").description(UNMAPPED_EXCEPTION).build(),
                new ResponseBuilder().code("400").description("Some parameter was not informed correctly.").build()
        );
    }

    private ApiInfo metaInfo() {
        return new ApiInfo(
                "Wishlist - API",
                "Rest API para manutenção de wishlist de produtos.",
                "1.0",
                "Terms of Service",
                (new Contact("Vanderson Sampaio", "http://www.vandersonsampaio.com.br", "vandersons.sampaio@gmail.com")),
                "Apache License Version 2.0",
                "https://www.apache.org/licenses/",
                new ArrayList<>()
        );
    }
}
