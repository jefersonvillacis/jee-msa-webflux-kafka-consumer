package com.villacis.webflux.infrastructure.output.repository;

import com.villacis.webflux.infrastructure.output.repository.entity.MessageEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Jeferson Villacis
 * @version 1.0
 * @since 09/08/2025 Copyright 2025
 */
@Repository
public interface MessageRepository extends ReactiveCrudRepository<MessageEntity, Long> {
}
