package com.villacis.webflux.application.output.port;

import com.villacis.webflux.domain.model.MessageDto;
import reactor.core.publisher.Mono;

/**
 * @author Jeferson Villacis
 * @version 1.0
 * @since 09/08/2025 Copyright 2025
 */
public interface MessageOutputPort {
    Mono<Void> save(MessageDto messageDto);
}
