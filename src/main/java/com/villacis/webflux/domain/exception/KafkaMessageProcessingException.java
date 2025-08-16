package com.villacis.webflux.domain.exception;

import org.springframework.http.HttpStatus;

/**
 * @author Jeferson Villacis
 * @version 1.0
 * @since 10/08/2025 Copyright 2025
 */
public class KafkaMessageProcessingException extends AbstractRuntimeException {
  public KafkaMessageProcessingException(Throwable throwable) {
    super("An error occurred while trying to process the message", throwable, HttpStatus.BAD_REQUEST);
  }
}
