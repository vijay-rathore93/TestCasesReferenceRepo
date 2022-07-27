package com.user.usermanagement.configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;

@Configuration
@PropertySource("classpath:swagger.properties")
public class SwaggerConfig {

    @Value("${swagger.apiInfo.title}")
    private String apiInfoTitle;

    @Value("${swagger.apiInfo.description}")
    private String apiInfoDescription;

    @Value("${swagger.apiInfo.version}")
    private String apiInfoVersion;

    @Value("${swagger.apiInfo.termsOfServiceUrl}")
    private String apiInfoTermsOfServiceUrl;

    @Value("${swagger.apiInfo.contact.name}")
    private String apiInfoContactName;

    @Value("${swagger.apiInfo.contact.url}")
    private String apiInfoContactUrl;

    @Value("${swagger.apiInfo.contact.email}")
    private String apiInfoContactEmail;

    @Value("${swagger.apiInfo.license}")
    private String apiInfoLicense;

    @Value("${swagger.apiInfo.licenseUrl}")
    private String apiInfoLicenseUrl;

    @Bean
    public OpenAPI customSwaggerConfiguration() {
        return new OpenAPI().components(new Components()).info(
            new Info()
                .title(apiInfoTitle)
                .description(apiInfoDescription)
                .contact(new Contact().email(apiInfoContactEmail).url(apiInfoContactUrl).name(apiInfoContactName))
                .license(new License().name(apiInfoLicense).url(apiInfoLicenseUrl))
                .version(apiInfoVersion)
        );
    }
}
