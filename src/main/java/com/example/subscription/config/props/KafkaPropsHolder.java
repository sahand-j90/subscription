package com.example.subscription.config.props;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author Sahand Jalilvand 16.01.24
 */
@Component
@ConfigurationProperties(prefix = KafkaPropsHolder.PREFIX)
@Getter
@Setter
public class KafkaPropsHolder {

    public static final String PREFIX = "subscription.kafka";

    List<String> kafkaHosts;

    int partitions;

    int replicas;

    int timeout;

    boolean failFast;

    int retention;
}
