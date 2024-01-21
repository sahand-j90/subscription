package com.example.subscription.integration;

import com.example.subscription.config.TestKafkaConfig;
import com.redis.testcontainers.RedisContainer;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.io.File;
import java.nio.file.Path;

/**
 * @author Sahand Jalilvand 21.01.24
 */
@SpringBootTest
@Import(TestKafkaConfig.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
public abstract class AbstractIntegrationTest {

    @Container
    static GenericContainer<?> postgres = new GenericContainer<>("debezium/postgres")
            .withEnv("POSTGRES_USER", "test1")
            .withEnv("POSTGRES_PASSWORD", "test1")
            .withEnv("POSTGRES_DB", "test1")
            .withExposedPorts(5432);


    @Container
    static KafkaContainer kafka = new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:7.4.0")).
            withKraft();

    @Container
    static RedisContainer redis = new RedisContainer(DockerImageName.parse("redis:6.0.9"));

    @TempDir
    static Path sharedTempDir;

    @BeforeAll
    static void beforeAll() {
        if (!postgres.isRunning()) {
            postgres.start();
        }

        if (!kafka.isRunning()) {
            kafka.start();
        }

        if (!redis.isRunning()) {
            redis.start();
        }
    }


    @DynamicPropertySource
    @SneakyThrows
    static void configureProperties(DynamicPropertyRegistry registry) {

        // Postgres Properties
        registry.add("subscription.rdbms.server", postgres::getHost);
        registry.add("subscription.rdbms.port", postgres::getFirstMappedPort);

        // Kafka Properties
        registry.add("subscription.kafka.kafka-hosts", () -> kafka.getHost() + ":" + kafka.getFirstMappedPort());

        // Redis Properties
        registry.add("spring.data.redis.host", redis::getHost);
        registry.add("spring.data.redis.port", redis::getFirstMappedPort);

        // offset file
        File tempFile = sharedTempDir.resolve("offset.dat").toFile();
        tempFile.createNewFile();
        registry.add("subscription.cdc.offset-file", tempFile::getAbsolutePath);
    }


}
