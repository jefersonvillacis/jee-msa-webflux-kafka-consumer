package com.villacis.webflux.infrastructure.input.adapter.kafka.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.reactive.ReactiveKafkaConsumerTemplate;
import reactor.kafka.receiver.ReceiverOptions;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Jeferson Villacis
 * @version 1.0
 * @since 09/08/2025 Copyright 2025
 */
@Slf4j
@EnableKafka
@Configuration
@RequiredArgsConstructor
public class KafkaConfig {
  private final KafkaPropertiesConfig kafkaProperties;

  @Bean
  public ReactiveKafkaConsumerTemplate<String, String> reactiveKafkaConsumerTemplate() {
    Map<String, Object> consumerProperties = new HashMap<>();
    consumerProperties.put(
        ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaProperties.getBootstrapServers());
    consumerProperties.put(
        ConsumerConfig.GROUP_ID_CONFIG, kafkaProperties.getTopic().getName() + "-group");
    consumerProperties.put(
        ConsumerConfig.AUTO_OFFSET_RESET_CONFIG,
        kafkaProperties.getConsumer().getAutoOffsetReset());
    consumerProperties.put(
        ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,
        kafkaProperties.getConsumer().getKeyDeserializer());
    consumerProperties.put(
        ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
        kafkaProperties.getConsumer().getValueDeserializer());
    ReceiverOptions<String, String> receiverOptions =
        ReceiverOptions.<String, String>create(consumerProperties)
            .subscription(Collections.singleton(kafkaProperties.getTopic().getName()));
    return new ReactiveKafkaConsumerTemplate<>(receiverOptions);
  }
}
