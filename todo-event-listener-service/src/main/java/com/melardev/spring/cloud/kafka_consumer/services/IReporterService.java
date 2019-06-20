package com.melardev.spring.cloud.kafka_consumer.services;


import com.melardev.spring.cloud.kafka_consumer.models.Todo;

public interface IReporterService {
    void reportTodoCreated(Todo todo);
}
