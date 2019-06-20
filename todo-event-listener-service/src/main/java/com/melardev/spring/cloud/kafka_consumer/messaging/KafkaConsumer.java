package com.melardev.spring.cloud.kafka_consumer.messaging;

import com.melardev.spring.cloud.kafka_consumer.models.Todo;
import com.melardev.spring.cloud.kafka_consumer.services.IReporterService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumer {

    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaConsumer.class);

    @Autowired
    @Qualifier("console")
    private IReporterService reporterService;

    @KafkaListener(topics = "${app.kafka.topics.todo-created}", containerFactory = "kafkaListenerContainerFactory")
    public void onTodoCreated(Todo todo) {
        LOGGER.info("KafkaConsumer::onTodoCreated");
        reporterService.reportTodoCreated(todo);
    }
}
