package com.example.subscription.integration;

import com.example.subscription.common.TransactionalService;
import com.example.subscription.enums.SubscriptionStateEnum;
import com.example.subscription.repositories.OutboxRepository;
import com.example.subscription.utils.KafkaTestUtils;
import com.example.subscription.utils.OutboxTestUtils;
import com.example.subscription.utils.SubscriptionTestUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.assertj.core.api.Assertions;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;

import java.time.Duration;
import java.util.UUID;

/**
 * @author Sahand Jalilvand 21.01.24
 */
public class OutboxIntegrationTest extends AbstractIntegrationTest {


    @Autowired
    KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    OutboxRepository outboxRepository;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    TransactionalService transactionalService;

    @Value("${subscription.domain-event-channel}")
    String domainEventChannel;

    @BeforeEach
    public void before() {
        transactionalService.doInTransaction(() -> outboxRepository.deleteAll());
    }

    @AfterEach
    public void after() {
        transactionalService.doInTransaction(() -> outboxRepository.deleteAll());
    }

    @SneakyThrows
    @Test
    public void GivenNewOutBoxRecordInserted_WhenTransactionCommitted_ThenConsumerShouldReceiveDomainEvent() {

        // GIVEN
        var correlationId = UUID.randomUUID().toString();
        var subscription = SubscriptionTestUtils.newSubscription(correlationId, SubscriptionStateEnum.ACTIVE);
        var payload = objectMapper.writeValueAsString(subscription);
        var outbox = OutboxTestUtils.newOutbox(correlationId, payload);

        var offset = KafkaTestUtils.lastOffset(kafkaTemplate, domainEventChannel, 0);

        // WHEN
        transactionalService.doInTransaction(() -> outboxRepository.save(outbox));

        // THEN
        Awaitility.await("wait_for_getting_domain_event")
                .atMost(Duration.ofSeconds(10))
                .untilAsserted(() -> {
                    var message = kafkaTemplate.receive(domainEventChannel, 0, offset, Duration.ofSeconds(5));

                    Assertions.assertThat(message).isNotNull();
                    var jsonNode = objectMapper.readTree(message.value());

                    var messageCorrelationId = jsonNode.get("correlationId").textValue();
                    Assertions.assertThat(messageCorrelationId).isEqualTo(correlationId);
                });
    }
}
