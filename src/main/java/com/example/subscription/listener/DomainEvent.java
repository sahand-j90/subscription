package com.example.subscription.listener;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.event.spi.EventSource;

/**
 * @author Sahand Jalilvand 16.01.24
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DomainEvent<T> {

    String correlationId;

    String domain;

    String eventType;

    T payload;

    EventSource session;
}
