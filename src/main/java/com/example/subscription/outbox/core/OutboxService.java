package com.example.subscription.outbox.core;

import com.example.subscription.common.TracerService;
import com.example.subscription.domains.OutboxEntity;
import com.example.subscription.outbox.DomainEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.FlushModeType;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

/**
 * @author Sahand Jalilvand 16.01.24
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class OutboxService {

    private final ObjectMapper objectMapper;
    private final TracerService tracerService;

    @EventListener(classes = DomainEvent.class)
    @Transactional(propagation = Propagation.MANDATORY)
    public void persistEvent(DomainEvent<?> domainEvent) {

        var outbox = OutboxEntity.builder()
                .idempotentKey(UUID.randomUUID())
                .createdAt(new Date())
                .correlationId(domainEvent.getCorrelationId())
                .domain(domainEvent.getDomain())
                .eventType(domainEvent.getEventType())
                .messageData(getPayload(domainEvent.getPayload()))
                .spanId(getSpanId())
                .traceId(getTraceId())
                .build();

        insert(outbox, domainEvent.getSession());
    }

    private void insert(OutboxEntity outbox, Session session) {
        var query = session.createNamedQuery(OutboxEntity.INSERT, Void.class);
        query.setParameter("idempotentKey", UUID.randomUUID());
        query.setParameter("createdAt", new Date());
        query.setParameter("correlationId", outbox.getCorrelationId());
        query.setParameter("domain", outbox.getDomain());
        query.setParameter("eventType", outbox.getEventType());
        query.setParameter("messageData", outbox.getMessageData());
        query.setParameter("spanId", outbox.getSpanId());
        query.setParameter("traceId", outbox.getTraceId());
        query.setFlushMode(FlushModeType.COMMIT);
        query.executeUpdate();
    }

    @SneakyThrows
    private <T> String getPayload(T t) {
        return objectMapper.writeValueAsString(t);
    }

    private String getSpanId() {
        return Optional.ofNullable(tracerService.getSpanIdAsLong())
                .map(Object::toString)
                .orElse("1");
    }

    private String getTraceId() {
        return Optional.ofNullable(tracerService.getTraceIdAsLong())
                .map(Object::toString)
                .orElse("1");
    }
}
