package com.example.subscription.utils;

import org.springframework.kafka.core.KafkaTemplate;

import java.time.Duration;

/**
 * @author Sahand Jalilvand 21.01.24
 */
public class KafkaTestUtils {

    public static int lastOffset(KafkaTemplate<String, String> kafkaTemplate, String topic, int partition) {
        int offset = 0;
        while (true) {
            var message = kafkaTemplate.receive(topic, partition, offset);

            if (message == null) {
                return offset;
            } else {
                offset++;
            }
        }
    }

}
