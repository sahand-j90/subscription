package com.example.subscription.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author Sahand Jalilvand 16.01.24
 */
@EnableConfigurationProperties
@EnableJpaAuditing
@Configuration
@EnableScheduling
public class GeneralConfig {
}
