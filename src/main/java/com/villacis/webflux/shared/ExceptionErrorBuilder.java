package com.villacis.webflux.shared;

import com.villacis.webflux.domain.model.ErrorDetailDto;
import com.villacis.webflux.domain.model.ErrorDto;
import org.springframework.http.HttpStatus;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.server.ServerWebExchange;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author Jeferson Villacis
 * @version 1.0
 * @since 08/07/2025 Copyright 2025
 */
public class ExceptionErrorBuilder {
  private static final String EXCEPTION_TITLE = "Kafka Producer Exception";
  private static final String UNKNOWN = "Unknown";

  private String message;
  private Throwable throwable;
  private List<ErrorDetailDto> errors;
  private HttpStatus httpStatus;
  private HandlerMethod handlerMethod;
  private ServerWebExchange serverWebExchange;

  private ExceptionErrorBuilder() {
    this.errors = new ArrayList<>();
  }

  public static ExceptionErrorBuilder builder() {
    return new ExceptionErrorBuilder();
  }

  public ExceptionErrorBuilder withThrowable(Throwable throwable) {
    this.throwable = throwable;
    return this;
  }

  public ExceptionErrorBuilder withMessage(String message) {
    this.message = message;
    return this;
  }

  public ExceptionErrorBuilder withErrors(List<ErrorDetailDto> errors) {
    this.errors = Objects.nonNull(errors) ? errors : new ArrayList<>();
    return this;
  }

  public ExceptionErrorBuilder withHttpStatus(HttpStatus httpStatus) {
    this.httpStatus = httpStatus;
    return this;
  }

  public ExceptionErrorBuilder withHandlerMethod(HandlerMethod handlerMethod) {
    this.handlerMethod = handlerMethod;
    return this;
  }

  public ExceptionErrorBuilder withServerWebExchange(ServerWebExchange serverWebExchange) {
    this.serverWebExchange = serverWebExchange;
    return this;
  }

  public ErrorDto build() {
    return new ErrorDto(
        EXCEPTION_TITLE,
        getDetailMessage(),
        getStatusCode(),
        getPath(),
        getResourceName(),
        getComponentName(),
        this.errors);
  }

  private String getDetailMessage() {
    if (Objects.nonNull(this.message)) {
      return this.message;
    }
    if (Objects.nonNull(this.throwable)) {
      return getThrowableMessage(this.throwable);
    }
    return UNKNOWN;
  }

  private String getStatusCode() {
    return Objects.nonNull(this.httpStatus)
        ? String.valueOf(this.httpStatus.value())
        : String.valueOf(HttpStatus.BAD_REQUEST.value());
  }

  private String getResourceName() {
    if (Objects.nonNull(this.serverWebExchange)) {
      return this.serverWebExchange.getRequest().getMethod().name();
    }
    return UNKNOWN;
  }

  private String getPath() {
    if (Objects.nonNull(this.serverWebExchange)) {
      return this.serverWebExchange.getRequest().getPath().value();
    }
    return UNKNOWN;
  }

  private String getComponentName() {
    return Objects.nonNull(this.handlerMethod)
        ? this.handlerMethod.getBeanType().getSimpleName()
        : UNKNOWN;
  }

  private static String getThrowableMessage(Throwable throwable) {
    if (Objects.nonNull(throwable.getMessage())) {
      return throwable.getMessage();
    }
    Throwable cause = throwable.getCause();
    return Objects.nonNull(cause) && Objects.nonNull(cause.getMessage())
        ? cause.getMessage()
        : UNKNOWN;
  }
}
