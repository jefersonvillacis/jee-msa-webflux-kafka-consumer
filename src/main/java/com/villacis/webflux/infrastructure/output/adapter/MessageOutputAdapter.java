package com.villacis.webflux.infrastructure.output.adapter;

import com.villacis.webflux.application.output.port.MessageOutputPort;
import com.villacis.webflux.domain.model.MessageDto;
import com.villacis.webflux.infrastructure.output.repository.MessageRepository;
import com.villacis.webflux.infrastructure.output.repository.entity.MessageEntity;
import com.villacis.webflux.shared.MessageJsonSerializer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

/**
 * @author Jeferson Villacis
 * @version 1.0
 * @since 10/08/2025 Copyright 2025
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class MessageOutputAdapter implements MessageOutputPort {
  private final MessageRepository messageRepository;
  private final MessageJsonSerializer messageJsonSerializer;

  @Override
  public Mono<Void> save(MessageDto messageDto) {
    return messageRepository
        .save(
            MessageEntity.builder()
                .createdAt(messageDto.createdAt())
                .topic(messageDto.topic())
                .headers(messageDto.headers())
                .message(messageDto.message())
                .build())
        .doOnSuccess(
            messageSaved ->
                log.info(
                    "Message processed and saved successfully in the database: {}",
                    messageJsonSerializer.toJsonWithPrettyPrinter(messageSaved)))
        .then();
  }
}
