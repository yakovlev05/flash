package ru.yakovlev05.school.flash.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.kafka.DefaultKafkaProducerFactoryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.support.serializer.JsonSerializer;

@RequiredArgsConstructor
@Configuration
@EnableKafka
public class KafkaProducerConfig {

    private final ObjectMapper objectMapper;

    @Bean
    public DefaultKafkaProducerFactoryCustomizer kafkaProducerFactoryCustomizer() {
        return producerFactory -> producerFactory.setValueSerializer(new JsonSerializer<>(objectMapper));
    }
}
