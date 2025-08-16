package com.villacis.webflux.domain.exception;

import org.springframework.http.HttpStatus;

/**
 * @author Jeferson Villacis
 * @version 1.0
 * @since 10/08/2025 Copyright 2025
 */
@lombok.Getter
public abstract class AbstractRuntimeException extends RuntimeException {
  private final transient HttpStatus status;

  protected AbstractRuntimeException(String message, HttpStatus status) {
    super(message);
    this.status = status;
  }

  @SuppressWarnings("unused")
  protected AbstractRuntimeException(String message, Throwable cause, HttpStatus status) {
    super(message, cause);
    this.status = status;
  }
}
