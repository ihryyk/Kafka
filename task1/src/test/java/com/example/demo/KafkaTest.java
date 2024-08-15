package com.example.demo;


import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.kafka.KafkaContainer;
import org.testcontainers.utility.DockerImageName;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

@Testcontainers
@SpringBootTest
public class KafkaTest {

    private static final DockerImageName DOCKER_IMAGE_NAME =
            DockerImageName.parse("confluentinc/cp-kafka:7.4.0")
                    .asCompatibleSubstituteFor("apache/kafka");

    @Container
    public static KafkaContainer kafka = new KafkaContainer(DOCKER_IMAGE_NAME);

    @Test
    public void testKafkaProducerAndConsumer() throws InterruptedException {
        String testTopic = "kafka_topic";
        String testKey = "test-key";
        String testValue = "test-value";

        produceTestMessage(testTopic, testKey, testValue);
        ConsumerRecord<String, String> record = consumeTestMessage(testTopic);

        Assertions.assertEquals(testKey, record.key());
        Assertions.assertEquals(testValue, record.value());
    }

    public void produceTestMessage(String topic, String key, String value) {
        Properties producerProps = new Properties();
        producerProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafka.getBootstrapServers());
        producerProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        producerProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);

        try (KafkaProducer<String, String> producer = new KafkaProducer<>(producerProps)) {
            producer.send(new ProducerRecord<>(topic, key, value));
            producer.flush();
        }
    }

    public ConsumerRecord<String, String> consumeTestMessage(String topic) {
        Properties consumerProps = new Properties();
        consumerProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafka.getBootstrapServers());
        consumerProps.put(ConsumerConfig.GROUP_ID_CONFIG, "test");
        consumerProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        consumerProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);

        try (KafkaConsumer<String, String> consumer = new KafkaConsumer<>(consumerProps)) {
            consumer.subscribe(Collections.singletonList(topic));
            ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));
            Assertions.assertEquals(1, records.count());
            return records.iterator().next();
        }
    }

}
