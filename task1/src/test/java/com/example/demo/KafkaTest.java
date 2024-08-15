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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.kafka.KafkaContainer;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

@SpringBootTest
@ContextConfiguration(classes = KafkaContainerConfiguration.class)
public class KafkaTest {

    @Autowired
    private KafkaContainer kafkaContainer;

    @Test
    public void testKafkaProducerAndConsumer() throws InterruptedException {
        kafkaContainer.start();
        Properties producerProps = new Properties();
        producerProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaContainer.getBootstrapServers());
        producerProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        producerProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);

        KafkaProducer<String, String> producer = new KafkaProducer<>(producerProps);

        String testTopic = "testTopic";
        String testKey = "test-key";
        String testValue = "test-value";
        producer.send(new ProducerRecord<>(testTopic, testKey, testValue));
        producer.flush();
        producer.close();

        Properties consumerProps = new Properties();
        consumerProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaContainer.getBootstrapServers());
        consumerProps.put(ConsumerConfig.GROUP_ID_CONFIG, "test");
        consumerProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        consumerProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(consumerProps);

        consumer.subscribe(Collections.singletonList(testTopic));

        // wait for Kafka to propagate the produced message to the consumers
        Thread.sleep(1000);

        ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));
        Assertions.assertEquals(1, records.count());

        ConsumerRecord<String, String> record = records.iterator().next();
        Assertions.assertEquals(testKey, record.key());
        Assertions.assertEquals(testValue, record.value());

        consumer.close();
    }

}
