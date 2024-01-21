package com.example.subscription.utils;

import com.example.subscription.domains.SubscriptionEntity;
import com.example.subscription.enums.SubscriptionStateEnum;

import java.time.LocalDate;
import java.util.Date;
import java.util.UUID;

/**
 * @author Sahand Jalilvand 21.01.24
 */
public class SubscriptionTestUtils {

    public static SubscriptionEntity newSubscription(String correlationId, SubscriptionStateEnum state) {
        return SubscriptionEntity.builder()
                .id(UUID.fromString(correlationId))
                .createdAt(new Date())
                .updatedAt(new Date())
                .version(0)
                .state(state)
                .from(LocalDate.now())
                .to(LocalDate.now().plusDays(1))
                .build();
    }

}
