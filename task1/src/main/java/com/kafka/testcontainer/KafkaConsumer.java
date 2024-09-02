package com.kafka.testcontainer;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@Data
public class KafkaConsumer {

    private String payload;

    @KafkaListener(topics = "${kafka.topic}", groupId = "test_group_id")
    public void receive(ConsumerRecord<?, ?> consumerRecord) {
        String message = consumerRecord.toString();
        payload = message;
        log.info(" received payload='{}'", message);
    }
}
