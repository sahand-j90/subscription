package com.example.subscription.security.conf;

import com.example.subscription.enums.UserAuthorityEnum;
import com.example.subscription.i18n.MessageResolver;
import com.example.subscription.security.filters.JwtRequestFilter;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.annotation.web.configurers.ExceptionHandlingConfigurer;
import org.springframework.security.config.annotation.web.configurers.SessionManagementConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

/**
 * @author Sahand Jalilvand 20.01.24
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true, jsr250Enabled = true)
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtRequestFilter jwtRequestFilter;
    private final List<AuthenticationProvider> authenticationProviders;
    private final MessageResolver messageResolver;
    private final ObjectMapper objectMapper;

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity security) throws Exception {
        var authenticationManagerBuilder = security.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationProviders.forEach(authenticationManagerBuilder::authenticationProvider);
        return authenticationManagerBuilder.build();
    }


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .authorizeHttpRequests(this::requestMatcher)

                .exceptionHandling(this::exceptionHandling)

                .sessionManagement(this::sessionManagement)

                .csrf(this::csrf)

                .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        var config = new CorsConfiguration();

        config.setAllowCredentials(true);
        config.addAllowedHeader("*");
        config.addAllowedOriginPattern("*");

        config.setAllowedMethods(Arrays.asList(
                HttpMethod.OPTIONS.name(),
                HttpMethod.HEAD.name(),
                HttpMethod.GET.name(),
                HttpMethod.PUT.name(),
                HttpMethod.POST.name(),
                HttpMethod.DELETE.name(),
                HttpMethod.PATCH.name()
        ));

        var source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }


    private void csrf(CsrfConfigurer<HttpSecurity> httpSecurityCsrfConfigurer) {
        httpSecurityCsrfConfigurer.disable();
    }

    private void sessionManagement(SessionManagementConfigurer<HttpSecurity> httpSecuritySessionManagementConfigurer) {
        httpSecuritySessionManagementConfigurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }


    private void exceptionHandling(ExceptionHandlingConfigurer<HttpSecurity> httpSecurityExceptionHandlingConfigurer) {
        httpSecurityExceptionHandlingConfigurer.accessDeniedHandler(new GeneralAccessDeniedHandler(objectMapper, messageResolver))
                .authenticationEntryPoint(new GeneralAuthenticationEntryPoint(objectMapper, messageResolver));
    }

    private void requestMatcher(AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry requestMatcherRegistry) {

        var matcher = requestMatcherRegistry.requestMatchers("/login/**")
                .permitAll();

        matcher.requestMatchers("/users/**")
                .hasAnyRole(UserAuthorityEnum.ADMIN.name());

        matcher.anyRequest().authenticated();
    }

}
