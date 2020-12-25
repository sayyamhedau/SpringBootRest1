package com.app.config;

import java.util.Collections;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

	@Bean
	public Docket apiDocket() {
		return new Docket(DocumentationType.SWAGGER_2)
				.groupName("Product-API")
				.select()
				.apis(RequestHandlerSelectors.basePackage("com.app.controller"))
				.paths(PathSelectors.regex("/rest/api.*"))
				.build()
				.apiInfo(getApiInfo());
	}

	private ApiInfo getApiInfo() {
		return new ApiInfo("Product Rest API Documentation", 
				"This is a sample Spring Boot RESTful service using SpringFox + Swagger2", 
				"v1.0", 
				"Terms Of Service", 
				new Contact("Sayyam Hedau", "", ""),
				"Apache License Version 2.0", "", 
				Collections.emptyList());
	}
}
