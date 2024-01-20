package com.example.subscription.config.openapi;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.parameters.Parameter;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpHeaders;
import org.springframework.web.method.HandlerMethod;

import java.util.ArrayList;
import java.util.List;

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
                .addOperationCustomizer(tokenHeaderOperationCustomizer())
                .displayName("User")
                .build();
    }

    @Bean
    public GroupedOpenApi subscriberGroupedOpenApi() {
        return GroupedOpenApi.builder()
                .group("subscriber")
                .pathsToMatch("/v1/subscribers/**")
                .addOperationCustomizer(tokenHeaderOperationCustomizer())
                .displayName("Subscriber")
                .build();
    }

    @Bean
    public GroupedOpenApi subscriptionGroupedOpenApi() {
        return GroupedOpenApi.builder()
                .group("subscription")
                .pathsToMatch("/v1/subscriptions/**")
                .addOperationCustomizer(tokenHeaderOperationCustomizer())
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

    private OperationCustomizer tokenHeaderOperationCustomizer() {
        return ((operation, handlerMethod) -> {

            List<Parameter> parameters = operation.getParameters();
            if (parameters == null) {
                parameters = new ArrayList<>();
            }
            var parameter = new Parameter();
            var schema = new Schema<>();
            schema.setType("string");
            parameter.setName("X-Token");
            parameter.setIn("header");
            parameter.setRequired(false);
            parameter.set$ref("");
            parameter.setDescription("JWT");
            parameter.setDeprecated(false);
            parameter.setSchema(schema);
            parameters.add(parameter);
            return operation;
        });
    };

}
