package com.example.subscription.utils;

import com.example.subscription.domains.SubscriberEntity;

/**
 * @author Sahand Jalilvand 21.01.24
 */
public class SubscriberTestUtils {

    public static SubscriberEntity newSubscriber() {
        return SubscriberEntity.builder()
                .subscriberName("test_subscriber")
                .build();
    }

}
