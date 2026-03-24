package com.villacis.webflux.infrastructure.input.adapter.kafka.config;

import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.reactive.ReactiveKafkaConsumerTemplate;
import reactor.kafka.receiver.ReceiverOptions;

import java.util.Collections;
import java.util.Map;

/**
 * @author Jeferson Villacis
 * @version 1.0
 * @since 09/08/2025 Copyright 2025
 */
@EnableKafka
@Configuration
public class KafkaConfig {
  private final KafkaProperties kafkaProperties;
  private final KafkaPropertiesConfig kafkaPropertiesConfig;

  public KafkaConfig(KafkaProperties kafkaProperties, KafkaPropertiesConfig kafkaPropertiesConfig) {
    this.kafkaProperties = kafkaProperties;
    this.kafkaPropertiesConfig = kafkaPropertiesConfig;
  }

  @Bean
  public ReactiveKafkaConsumerTemplate<String, String> reactiveKafkaConsumerTemplate() {
    Map<String, Object> consumerProperties = kafkaProperties.buildConsumerProperties();

    ReceiverOptions<String, String> receiverOptions =
        ReceiverOptions.<String, String>create(consumerProperties)
            .subscription(Collections.singleton(kafkaPropertiesConfig.getTopic().getName()));
    return new ReactiveKafkaConsumerTemplate<>(receiverOptions);
  }
}
