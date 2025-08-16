package com.villacis.webflux.shared;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.villacis.webflux.domain.exception.JsonMappingFailureException;

/**
 * @author Jeferson Villacis
 * @version 1.0
 * @since 08/07/2025 Copyright 2025
 */
public class ExceptionJsonSerializer {
  private ExceptionJsonSerializer() {
    throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
  }

  public static String toJsonWithPrettyPrinter(Object exceptionLogEntry) {
    try {
      return new ObjectMapper()
          .writerWithDefaultPrettyPrinter()
          .writeValueAsString(exceptionLogEntry);
    } catch (JsonProcessingException e) {
      throw new JsonMappingFailureException();
    }
  }
}
