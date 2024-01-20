package com.example.subscription.config.openapi;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * @author Sahand Jalilvand 20.01.24
 */
@Configuration
@RequiredArgsConstructor
@Profile("Documentation")
public class OpenApiConfiguration {

    @Bean
    public GroupedOpenApi authenticationGroupedOpenApi() {
        return GroupedOpenApi.builder()
                .group("authentication")
                .pathsToMatch("/v1/authentication/**")
                .displayName("Authentication")
                .build();
    }

    @Bean
    public GroupedOpenApi userGroupedOpenApi() {
        return GroupedOpenApi.builder()
                .group("user")
                .pathsToMatch("/v1/users/**")
                .displayName("User")
                .build();
    }

    @Bean
    public GroupedOpenApi subscriberGroupedOpenApi() {
        return GroupedOpenApi.builder()
                .group("subscriber")
                .pathsToMatch("/v1/subscribers/**")
                .displayName("Subscriber")
                .build();
    }

    @Bean
    public GroupedOpenApi subscriptionGroupedOpenApi() {
        return GroupedOpenApi.builder()
                .group("subscription")
                .pathsToMatch("/v1/subscriptions/**")
                .displayName("Subscription")
                .build();
    }


    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(apiInfo());
    }


    private Info apiInfo() {
        return new Info()
                .title("Subscription with Outbox Pattern")
                .description("Subscription with Outbox Pattern")
                .version("1.0.0");
    }

}
