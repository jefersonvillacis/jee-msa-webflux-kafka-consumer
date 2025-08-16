package com.villacis.webflux.application.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.villacis.webflux.application.input.port.MessageInputPort;
import com.villacis.webflux.application.output.port.MessageOutputPort;
import com.villacis.webflux.domain.exception.KafkaMessageProcessingException;
import com.villacis.webflux.domain.exception.KafkaMessageContentProcessingException;
import com.villacis.webflux.domain.exception.KafkaMessageHeadersProcessingException;
import com.villacis.webflux.domain.model.MessageDto;
import com.villacis.webflux.infrastructure.input.adapter.kafka.config.KafkaPropertiesConfig;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.header.Header;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Jeferson Villacis
 * @version 1.0
 * @since 10/08/2025 Copyright 2025
 */
@Service
@RequiredArgsConstructor
public class MessageService implements MessageInputPort {
  private final MessageOutputPort messageOutputPort;
  private final ObjectMapper objectMapper;
  private final KafkaPropertiesConfig kafkaProperties;

  @Override
  public Mono<Void> processMessage(ConsumerRecord<String, String> consumerRecord) {
    return getMessageContent(consumerRecord.value())
        .flatMap(
            messageContent ->
                extractAllowedHeaders(consumerRecord)
                    .map(
                        messageHeaders ->
                            new MessageDto(
                                LocalDateTime.now(),
                                consumerRecord.topic(),
                                messageHeaders,
                                messageContent))
                    .flatMap(messageOutputPort::save))
        .onErrorMap(KafkaMessageProcessingException::new)
        .then();
  }

  private Mono<String> getMessageContent(String rawMessage) {
    return Mono.just(
            new String(rawMessage.getBytes(StandardCharsets.UTF_8), StandardCharsets.UTF_8))
        .flatMap(
            stringMessage ->
                Mono.fromCallable(
                    () -> objectMapper.writeValueAsString(objectMapper.readTree(stringMessage))))
        .onErrorMap(KafkaMessageContentProcessingException::new);
  }

  private Mono<String> extractAllowedHeaders(ConsumerRecord<String, String> consumerRecord) {
    return Mono.fromCallable(
            () -> {
              Map<String, String> headers = new HashMap<>();
              kafkaProperties
                  .getAllowedHeaders()
                  .forEach(
                      headerKey -> {
                        Header header = consumerRecord.headers().lastHeader(headerKey);
                        if (header != null) {
                          headers.put(
                              headerKey, new String(header.value(), StandardCharsets.UTF_8));
                        }
                      });
              return objectMapper.writeValueAsString(headers);
            })
        .onErrorMap(KafkaMessageHeadersProcessingException::new);
  }
}
