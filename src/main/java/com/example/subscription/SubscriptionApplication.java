package com.example.subscription;

import com.example.subscription.domains.SubscriberEntity;
import com.example.subscription.services.dto.SubscriberDto;
import com.example.subscription.services.dto.SubscriptionDto;
import com.example.subscription.services.mapper.SubscriberMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.UUID;

@SpringBootApplication
@Slf4j
public class SubscriptionApplication {

    public static void main(String[] args) {
        SpringApplication.run(SubscriptionApplication.class, args);
    }

    @Autowired
    SubscriberMapper subscribeMapping;

    @Bean
    public CommandLineRunner init(@Value("${spring.application.name}") String appName) {
        return args -> {
            System.out.println("xxxx");

            var entity  = SubscriberEntity.builder()
                    .id(UUID.randomUUID())
                    .createdAt(new Date())
                    .updatedAt(new Date())
                    .subscriberName("sahand")
                    .version(1)
                    .subscriptions(Collections.emptyList())
                    .build();

            var dto = SubscriberDto.builder()
                    .id(UUID.randomUUID())
                    .createdAt(new Date())
                    .updatedAt(new Date())
                    .subscriberName("sahand")
                    .version(1)
                    .build();

            var newDto = subscribeMapping.toDto(new ArrayList<>(){{add(entity);}});

            var newEntity = subscribeMapping.toEntity(new ArrayList<>(){{add(dto);}});

            System.out.println("xxx");

        };
    }
}
