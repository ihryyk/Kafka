package com.kafka.testcontainer;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;

@SpringBootTest
@Testcontainers
public class KafkaTest {

    private static final String TOPIC = "test";

    @Container
    public static KafkaContainer kafka =
            new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:7.0.0"));

    @Autowired
    private KafkaConsumer consumer;

    @Autowired
    private KafkaProducer producer;

//    @DynamicPropertySource
//    public static void setKafkaProperties(DynamicPropertyRegistry registry) {
//        registry.add("spring.kafka.bootstrap-servers", kafka::getBootstrapServers);
//    }

    @Test
    public void testKafkaProducerAndConsumer() throws InterruptedException {
        String data = "Sending with our own simple KafkaProducer";
        producer.send(TOPIC, data);
        Thread.sleep(5000);
        assertThat(consumer.getPayload(), containsString(data));

    }

}