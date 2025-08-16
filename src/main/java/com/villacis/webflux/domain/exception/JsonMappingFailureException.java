package com.villacis.webflux.domain.exception;

import org.springframework.http.HttpStatus;

/**
 * @author Jeferson Villacis
 * @version 1.0
 * @since 10/08/2025 Copyright 2025
 */
public class JsonMappingFailureException extends AbstractRuntimeException {
  public JsonMappingFailureException() {
    super("An error occurred while trying to convert the object to string", HttpStatus.BAD_REQUEST);
  }
}
