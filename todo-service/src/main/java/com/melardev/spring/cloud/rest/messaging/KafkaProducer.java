package com.melardev.spring.cloud.rest.messaging;


import com.melardev.spring.cloud.rest.config.KafkaProducerConfig;
import com.melardev.spring.cloud.rest.entities.Todo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;


public class KafkaProducer {

    @Autowired
    KafkaProducerConfig kafkaProducerConfig;
    @Autowired
    private KafkaTemplate<String, Todo> simpleKafkaTemplate;

    public void sendCreated(Todo payload) {
        simpleKafkaTemplate.send(kafkaProducerConfig.getTodoCreatedTopic(), payload);
    }
}
