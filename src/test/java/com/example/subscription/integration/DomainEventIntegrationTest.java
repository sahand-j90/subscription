package com.example.subscription.integration;

import com.example.subscription.common.TransactionalService;
import com.example.subscription.config.TestKafkaConfig;
import com.example.subscription.domains.SubscriberEntity;
import com.example.subscription.enums.SubscriptionEventTypeEnum;
import com.example.subscription.repositories.OutboxRepository;
import com.example.subscription.repositories.SubscriberRepository;
import com.example.subscription.repositories.SubscriptionRepository;
import com.example.subscription.services.SubscriptionService;
import com.example.subscription.services.dto.CreateSubscriptionDto;
import com.example.subscription.utils.SubscriberTestUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.Assertions;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.kafka.core.KafkaTemplate;

import java.time.Duration;
import java.time.LocalDate;

/**
 * @author Sahand Jalilvand 21.01.24
 */
@SpringBootTest
@Import(TestKafkaConfig.class)
public class DomainEventIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    SubscriberRepository subscriberRepository;

    @Autowired
    SubscriptionRepository subscriptionRepository;

    @Autowired
    OutboxRepository outboxRepository;

    @Autowired
    TransactionalService transactionalService;

    @Autowired
    SubscriptionService subscriptionService;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    KafkaTemplate<String, String> kafkaTemplate;

    @Value("${subscription.domain-event-channel}")
    String domainEventChannel;


    SubscriberEntity subscriber;

    @BeforeEach
    public void before() {
        transactionalService.doInTransaction(() -> {

            subscriptionRepository.deleteAll();
            subscriberRepository.deleteAll();
            outboxRepository.deleteAll();

            var entity = SubscriberTestUtils.newSubscriber();
            this.subscriber = subscriberRepository.save(entity);
        });
    }

    @AfterEach
    public void after() {
        transactionalService.doInTransaction(() -> {
            subscriptionRepository.deleteAll();
            subscriberRepository.deleteAll();
            outboxRepository.deleteAll();
        });
    }

    @Test
    public void GivenNewSubscriptionIsCreated_WhenCommitted_ThenDomainEventsShouldReceivedByConsumer() {
        // GIVEN
        var createSubscription = new CreateSubscriptionDto();
        createSubscription.setSubscriberId(subscriber.getId());
        createSubscription.setFrom(LocalDate.now());
        createSubscription.setTo(LocalDate.now().plusDays(1));

        // WHEN
        var subscription = subscriptionService.create(createSubscription);

        // THEN
        Awaitility.await("wait_for_getting_domain_event")
                .atMost(Duration.ofSeconds(10))
                .untilAsserted(() -> {
                    var creationMessage = kafkaTemplate.receive(domainEventChannel, 0, 0, Duration.ofSeconds(5));

                    Assertions.assertThat(creationMessage).isNotNull();
                    var creationMessageJsonNode = objectMapper.readTree(creationMessage.value());

                    var messageCorrelationId = creationMessageJsonNode.get("correlationId").textValue();
                    var createdEventType = creationMessageJsonNode.get("eventType").textValue();
                    Assertions.assertThat(messageCorrelationId).isEqualTo(subscription.getId().toString());
                    Assertions.assertThat(createdEventType).isEqualTo(SubscriptionEventTypeEnum.CREATED.name());

                    var stateChangedMessage = kafkaTemplate.receive(domainEventChannel, 0, 1, Duration.ofSeconds(5));

                    Assertions.assertThat(stateChangedMessage).isNotNull();
                    var stateChangedMessageJsonNode = objectMapper.readTree(stateChangedMessage.value());

                    messageCorrelationId = stateChangedMessageJsonNode.get("correlationId").textValue();
                    var activatedEventType = stateChangedMessageJsonNode.get("eventType").textValue();
                    Assertions.assertThat(messageCorrelationId).isEqualTo(subscription.getId().toString());
                    Assertions.assertThat(activatedEventType).isEqualTo(SubscriptionEventTypeEnum.ACTIVATED.name());
                });
    }
}
