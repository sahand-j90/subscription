package com.example.subscription.config.kafka;

import com.example.subscription.config.props.KafkaPropsHolder;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.common.config.TopicConfig;
import org.apache.kafka.common.errors.TopicExistsException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

/**
 * @author Sahand Jalilvand 16.01.24
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class KafkaTopics implements InitializingBean {


    private final KafkaAdmin kafkaAdmin;
    private final KafkaPropsHolder kafkaPropsHolder;

    @Value("${subscription.domain-event-channel}")
    private String domainEventsChannel;

    @Value("${subscription.dead-letter-channel}")
    private String deadLetterChannel;

    @Override
    public void afterPropertiesSet() {
        createTopic(domainEventsChannel);
        createTopic(deadLetterChannel);
    }

    @SneakyThrows
    private void createTopic(String topicName) {

        try (AdminClient adminClient = AdminClient.create(kafkaAdmin.getConfigurationProperties())) {
            var topicConfig = new HashMap<String, String>();
            topicConfig.put(TopicConfig.RETENTION_MS_CONFIG, String.valueOf(kafkaPropsHolder.getRetention() * 24 * 60 * 60 * 1000));
            var newTopic = new NewTopic(topicName, kafkaPropsHolder.getPartitions(), (short) kafkaPropsHolder.getReplicas())
                    .configs(topicConfig);
            adminClient.createTopics(Collections.singletonList(newTopic)).all().get();
        } catch (ExecutionException e) {
            var cause = e.getCause();
            if (cause instanceof TopicExistsException) {
                log.info("Topic[{}] already exists", topicName);
            }
            else {
                throw cause;
            }
        }
    }

}
