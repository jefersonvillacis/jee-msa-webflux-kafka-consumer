package com.villacis.webflux.shared.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.villacis.webflux.domain.exception.JsonMappingFailureException;
import com.villacis.webflux.shared.MessageJsonSerializer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * @author Jeferson Villacis
 * @version 1.0
 * @since 10/08/2025 Copyright 2025
 */
@Component
@RequiredArgsConstructor
public class MessageJsonSerializerImpl implements MessageJsonSerializer {
  private final ObjectMapper objectMapper;

  public String toJsonWithPrettyPrinter(Object exceptionLogEntry) {
    try {
      return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(exceptionLogEntry);
    } catch (JsonProcessingException e) {
      throw new JsonMappingFailureException();
    }
  }
}
