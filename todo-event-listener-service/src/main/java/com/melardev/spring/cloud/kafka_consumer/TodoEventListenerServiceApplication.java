package com.melardev.spring.cloud.kafka_consumer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class TodoEventListenerServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(TodoEventListenerServiceApplication.class, args);
    }

}
