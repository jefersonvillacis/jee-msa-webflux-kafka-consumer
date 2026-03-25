package com.villacis.webflux.infrastructure.input.adapter.kafka.consumer;

import com.villacis.webflux.application.input.port.MessageInputPort;
import com.villacis.webflux.infrastructure.input.adapter.kafka.config.KafkaPropertiesConfig;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.kafka.core.reactive.ReactiveKafkaConsumerTemplate;
import org.springframework.stereotype.Component;
import reactor.core.Disposable;
import reactor.core.publisher.Mono;

/**
 * @author Jeferson Villacis
 * @version 1.0
 * @since 10/08/2025 Copyright 2025
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaConsumerAdapter {
  private Disposable subscription;
  private final ReactiveKafkaConsumerTemplate<String, String> reactiveKafkaConsumerTemplate;
  private final KafkaPropertiesConfig kafkaProperties;
  private final MessageInputPort messageInputPort;

  @EventListener(ApplicationReadyEvent.class)
  public void subscribeToTopic() {
    this.subscription =
        reactiveKafkaConsumerTemplate
            .receive()
            .groupBy(partition -> partition.receiverOffset().topicPartition())
            .flatMap(
                partitionFlux ->
                    partitionFlux.concatMap(
                        receiverRecord ->
                            consumeMessage(receiverRecord)
                                .onErrorResume(e -> Mono.empty())
                                .then(
                                    Mono.fromRunnable(
                                        () -> receiverRecord.receiverOffset().acknowledge()))),
                kafkaProperties.getConsumerStrategy().getMaxConcurrency())
            .subscribe();
  }

  @PreDestroy
  public void stopConsumer() {
    if (subscription != null && !subscription.isDisposed()) {
      log.info("Closing Kafka's reactive subscription");
      subscription.dispose();
    }
  }

  public Mono<Void> consumeMessage(ConsumerRecord<String, String> consumerRecord) {
    return messageInputPort.processMessage(consumerRecord).then();
  }
}
