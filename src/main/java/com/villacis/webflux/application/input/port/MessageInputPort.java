package com.villacis.webflux.application.input.port;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import reactor.core.publisher.Mono;

/**
 * @author Jeferson Villacis
 * @version 1.0
 * @since 09/08/2025 Copyright 2025
 */
public interface MessageInputPort {
    Mono<Void> processMessage(ConsumerRecord<String, String> consumerRecord);
}
