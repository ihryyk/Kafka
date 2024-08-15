package com.log.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class LogService {

    @KafkaListener(topics = "output", groupId = "result-group-id")
    public void consume(ConsumerRecord<String, Double> record) {
        log.info("VehicleId: {} Distance: {}", record.key(), record.value());
    }

}
