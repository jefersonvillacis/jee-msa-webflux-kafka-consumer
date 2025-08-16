package com.villacis.webflux.shared;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Jeferson Villacis
 * @version 1.0
 * @since 08/07/2025 Copyright 2025
 */
@Getter
@Setter
@Builder
public class ExceptionLogEntry {
  private String date;
  private String exceptionName;
  private String statusName;
  private String statusCode;
  private String message;
  private String cause;
  private String clazz;
  private String method;
  private int line;

  @Override
  public String toString() {
    return ExceptionJsonSerializer.toJsonWithPrettyPrinter(this);
  }
}
