package com.kafka.testcontainer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;

@EnableKafka
@SpringBootApplication
public class KafkaTestContainerApplication {

	public static void main(String[] args) {
		SpringApplication.run(KafkaTestContainerApplication.class, args);
	}

}
