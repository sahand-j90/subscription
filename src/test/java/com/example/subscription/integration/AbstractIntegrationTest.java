package com.example.subscription.integration;

import com.redis.testcontainers.RedisContainer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.utility.DockerImageName;

/**
 * @author Sahand Jalilvand 21.01.24
 */
public abstract class AbstractIntegrationTest {

    static GenericContainer<?> postgres = new GenericContainer<>("debezium/postgres")
            .withEnv("POSTGRES_USER", "test1")
            .withEnv("POSTGRES_PASSWORD", "test1")
            .withEnv("POSTGRES_DB", "test1")
            .withExposedPorts(5432);


    static KafkaContainer kafka = new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:5.4.3"));

    static RedisContainer redis = new RedisContainer(DockerImageName.parse("redis:6.0.9"));

    @BeforeAll
    static void beforeAll() {
        postgres.start();
        kafka.start();
        redis.start();
    }

    @AfterAll
    static void afterAll() {
        postgres.stop();
        kafka.stop();
        redis.stop();
    }


    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {

        // Postgres Properties
        registry.add("subscription.rdbms.server", postgres::getHost);
        registry.add("subscription.rdbms.port", postgres::getFirstMappedPort);

        // Kafka Properties
        registry.add("subscription.kafka.kafka-hosts", () -> kafka.getHost() + ":" + kafka.getFirstMappedPort());

        // Redis Properties
        registry.add("spring.data.redis.host", redis::getHost);
        registry.add("spring.data.redis.port", redis::getFirstMappedPort);
    }


}
