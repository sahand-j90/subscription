package com.example.subscription.outbox.core;

import com.example.subscription.domains.OutboxEntity;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;
import java.util.UUID;

/**
 * @author Sahand Jalilvand 16.01.24
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class CdcService {

    @Value("${subscription.domain-event-channel")
    private String domainEventChannel;

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public void publish(Map<String, Object> payload) {

        var outboxEntity = convert(payload);

        setSpanId(outboxEntity.getSpanId());

        var content = createContent(outboxEntity);

        kafkaTemplate.send(domainEventChannel, outboxEntity.getCorrelationId(), content)
                .whenComplete((result, ex) -> {

                    setSpanId(outboxEntity.getSpanId());

                    if (ex == null) {
                        handleSuccess(outboxEntity, result);
                    } else {
                        handleFailure(outboxEntity, ex);
                    }
                });

    }

    @SneakyThrows
    private String createContent(OutboxEntity outboxEntity) {
        return objectMapper.writeValueAsString(outboxEntity);
    }

    @SneakyThrows
    private OutboxEntity convert(Map<String, Object> payload) {

        var outbox = OutboxEntity.builder()
                .idempotentKey(UUID.fromString((String) payload.get("idempotent_key")))
                .correlationId((String) payload.get("correlation_id"))
                .createdAt(new Date((long) payload.get("created_at")))
                .eventType((String) payload.get("event_type"))
                .domain((String) payload.get("domain"))
                .spanId((String) payload.get("span_id"))
                .build();

        outbox.setPayload(objectMapper.readTree((String) payload.get("message_data")));

        return outbox;
    }

    private void handleSuccess(OutboxEntity outboxEntity, SendResult<String, String> result) {

        log.info("DomainEventSent: correlationId[{}] domain[{}] type[{}]",
                outboxEntity.getCorrelationId(),
                outboxEntity.getDomain(),
                outboxEntity.getEventType());

        log.debug("DomainEventSentDetails: outboxEntity[{}] result[{}]",
                outboxEntity,
                result.toString());
    }

    private void handleFailure(OutboxEntity outboxEntity, Throwable throwable) {

        // TODO: 18.01.24 Use dead letter pattern

        log.error("DomainEventException: correlationId[{}] outboxEntity[{}]",
                outboxEntity.getCorrelationId(),
                outboxEntity,
                throwable);
    }

    private void setSpanId(String spanId) {
        // TODO: 16.01.24
    }
}
