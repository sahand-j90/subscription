package com.example.subscription.utils;

import com.example.subscription.domains.OutboxEntity;

/**
 * @author Sahand Jalilvand 21.01.24
 */
public class OutboxTestUtils {

    public static OutboxEntity newOutbox(String correlationId, String payload) {
        return OutboxEntity.builder()
                .eventType("TestEvent")
                .domain("TestDomain")
                .correlationId(correlationId)
                .messageData(payload)
                .spanId("1")
                .traceId("1")
                .build();
    }

}
