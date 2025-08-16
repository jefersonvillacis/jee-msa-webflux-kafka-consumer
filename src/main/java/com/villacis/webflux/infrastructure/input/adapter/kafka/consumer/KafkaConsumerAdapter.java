package com.villacis.webflux.infrastructure.input.adapter.kafka.consumer;

import com.villacis.webflux.application.input.port.MessageInputPort;
import com.villacis.webflux.domain.exception.KafkaMessageProcessingException;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.core.reactive.ReactiveKafkaConsumerTemplate;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

/**
 * @author Jeferson Villacis
 * @version 1.0
 * @since 10/08/2025 Copyright 2025
 */
@Component
@RequiredArgsConstructor
public class KafkaConsumerAdapter {
  private final ReactiveKafkaConsumerTemplate<String, String> reactiveKafkaConsumerTemplate;
  private final MessageInputPort messageInputPort;

  @PostConstruct
  public void subscribeToTopic() {
    reactiveKafkaConsumerTemplate
        .receive()
        .flatMap(this::consumeMessage)
        .onErrorMap(KafkaMessageProcessingException::new)
        .subscribe();
  }

  public Mono<Void> consumeMessage(ConsumerRecord<String, String> consumerRecord) {
    return messageInputPort.processMessage(consumerRecord).then();
  }
}
