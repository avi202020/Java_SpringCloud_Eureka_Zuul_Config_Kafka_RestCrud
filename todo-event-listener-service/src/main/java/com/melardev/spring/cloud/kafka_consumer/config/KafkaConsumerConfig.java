package com.melardev.spring.cloud.kafka_consumer.config;


import com.melardev.spring.cloud.kafka_consumer.models.Todo;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.converter.DefaultJackson2JavaTypeMapper;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableKafka
public class KafkaConsumerConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    // Group Ids are very important if we want the messages to be broadcast to all receivers.
    // Using Group Ids a message coming from Kafka will be received by one consumer in that group.
    // Imagine the scenario where a consumer when triggered sends an email to the admin.
    // If we don't use group ids, then a message coming from Kafka it will be broadcast to all consumers
    // Each one will send an email!! Horrible, in cloud environments we create many instances of a microservice
    // to increase availability, if we don't create group id, then the message will be broadcast to all microservices
    // that is not the goal of micro services, we create microservices to pull off the hard work from a single service,
    // broadcasting is just the opposite, we make all microservices to react to same event!

    @Value("${spring.kafka.consumer.group-id}")
    private String consumerGroupId;


    @Bean
    public Map<String, Object> consumerConfigs() {
        // // https://docs.spring.io/spring-kafka/reference/html/
        Map<String, Object> properties = new HashMap<>();
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, consumerGroupId);
        // properties.put(JsonDeserializer.USE_TYPE_INFO_HEADERS, false);

        // What to do when there is not an initial offset or the offset does not exist
        // anymore for example when the data is deleted, earliest will reset the offset to the earliest
        // offset; other values: latest, none, anything else

        properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        // spring.kafka.producer.key-serializer=org.springframework.kafka.common.serialization.StringDeserializer
        // spring.kafka.producer.value-serializer=org.springframework.kafka.support.serializer.JsonDeserializer
        // spring.kafka.consumer.auto-offset-reset=earliest
        return properties;
    }

    @Bean
    public ConsumerFactory<String, Todo> consumerFactory() {

        JsonDeserializer<Todo> deserializer = new JsonDeserializer<>(Todo.class);
        deserializer.addTrustedPackages("com.melardev.spring.cloud.rest.entities");

        Map<String, Class<?>> mappings = new HashMap<>();
        mappings.put("com.melardev.spring.cloud.rest.entities.Todo", Todo.class);
        ((DefaultJackson2JavaTypeMapper) deserializer.getTypeMapper()).setIdClassMapping(mappings);
        return new DefaultKafkaConsumerFactory<>(consumerConfigs(), new StringDeserializer(),
                deserializer
        );

        // return new DefaultKafkaConsumerFactory<>(consumerConfigs());
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, Todo> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, Todo> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());

        return factory;
    }

    /*
    @Bean
    public JavaMailSender getJavaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost("smtp.gmail.com");
        mailSender.setPort(587);

        // Replace with your email and password
        mailSender.setUsername("non-existent-email-microservices@gmail.com");
        mailSender.setPassword("very_very_fake_password_remember_its_fake_");
        // Don't forget to enable less secure apps:
        // https://support.google.com/accounts/answer/6010255
        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.debug", "true");

        return mailSender;
    }
    */
}
