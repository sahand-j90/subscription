package com.example.subscription.config.kafka;

import com.example.subscription.config.props.KafkaPropsHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import java.util.HashMap;

/**
 * @author Sahand Jalilvand 16.01.24
 */
@Configuration
@RequiredArgsConstructor
@Slf4j
public class KafkaConfig {

    @Value("${spring.application.name}")
    private String appName;

    private final KafkaPropsHolder propsHolder;

    public ProducerFactory<String, String> kafkaProducerFactory() {

        var config = new HashMap<String, Object>();

        config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, propsHolder.getKafkaHosts());
        config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        config.put(ProducerConfig.ACKS_CONFIG, "all");
        config.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, "true");
        config.put(ProducerConfig.MAX_BLOCK_MS_CONFIG, propsHolder.getTimeout());
        config.put(ProducerConfig.REQUEST_TIMEOUT_MS_CONFIG, propsHolder.getTimeout());
        config.put(ProducerConfig.DELIVERY_TIMEOUT_MS_CONFIG, propsHolder.getTimeout());

        return new DefaultKafkaProducerFactory<>(config);
    }

    @Bean
    public KafkaTemplate<String, String> kafkaTemplate() {
        return new KafkaTemplate<>(kafkaProducerFactory());
    }

    @Bean
    public KafkaAdmin kafkaAdmin() {

        var config = new HashMap<String, Object>();

        config.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, propsHolder.getKafkaHosts());
        config.put(AdminClientConfig.REQUEST_TIMEOUT_MS_CONFIG, propsHolder.getTimeout());
        config.put(AdminClientConfig.DEFAULT_API_TIMEOUT_MS_CONFIG, propsHolder.getTimeout());
        config.put(AdminClientConfig.CLIENT_ID_CONFIG, appName);

        KafkaAdmin kafkaAdmin = new KafkaAdmin(config);
        kafkaAdmin.setFatalIfBrokerNotAvailable(propsHolder.isFailFast());

        return kafkaAdmin;
    }

}
