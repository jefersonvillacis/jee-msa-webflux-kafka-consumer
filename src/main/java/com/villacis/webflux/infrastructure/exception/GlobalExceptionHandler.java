package com.villacis.webflux.infrastructure.exception;

import com.villacis.webflux.domain.exception.AbstractRuntimeException;
import com.villacis.webflux.domain.exception.JsonMappingFailureException;
import com.villacis.webflux.domain.exception.KafkaMessageProcessingException;
import com.villacis.webflux.domain.exception.KafkaMessageContentProcessingException;
import com.villacis.webflux.domain.exception.KafkaMessageHeadersProcessingException;
import com.villacis.webflux.domain.model.ErrorDetailDto;
import com.villacis.webflux.domain.model.ErrorDto;
import com.villacis.webflux.shared.ExceptionErrorBuilder;
import com.villacis.webflux.shared.ExceptionLogEntryFactory;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.KafkaException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.server.ServerWebExchange;

import java.util.List;
import java.util.Objects;

/**
 * @author Jeferson Villacis
 * @version 1.0
 * @since 10/08/2025 Copyright 2025
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(NullPointerException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ErrorDto handleRuntimeException(RuntimeException ex, ServerWebExchange exchange) {
    log.error("Runtime Exception occurred: {}", ExceptionLogEntryFactory.createFromThrowable(ex));
    return ExceptionErrorBuilder.builder()
        .withThrowable(ex)
        .withHttpStatus(HttpStatus.BAD_REQUEST)
        .withServerWebExchange(exchange)
        .build();
  }

  @ExceptionHandler({
    JsonMappingFailureException.class,
    KafkaMessageProcessingException.class,
    KafkaMessageContentProcessingException.class,
    KafkaMessageHeadersProcessingException.class
  })
  public ResponseEntity<ErrorDto> handleAbstractRuntimeException(
      AbstractRuntimeException ex, HandlerMethod handlerMethod, ServerWebExchange exchange) {
    log.error(
        "Abstract runtime exception occurred: {}",
        ExceptionLogEntryFactory.createFromThrowable(ex));

    ExceptionErrorBuilder exceptionErrorBuilder =
        ExceptionErrorBuilder.builder()
            .withThrowable(ex)
            .withHttpStatus(HttpStatus.valueOf(ex.getStatus().value()))
            .withHandlerMethod(handlerMethod)
            .withServerWebExchange(exchange);

    if (Objects.nonNull(ex.getCause())) {
      Throwable cause = ex.getCause();
      List<ErrorDetailDto> errorDetailDto =
          List.of(
              new ErrorDetailDto(
                  String.valueOf(ex.getStatus().value()),
                  cause.getClass().getSimpleName(),
                  cause.getMessage()));
      exceptionErrorBuilder = exceptionErrorBuilder.withErrors(errorDetailDto);
    }
    return ResponseEntity.status(ex.getStatus()).body(exceptionErrorBuilder.build());
  }

  @ExceptionHandler(KafkaException.class)
  @ResponseStatus(HttpStatus.BAD_GATEWAY)
  public ErrorDto handleKafkaException(KafkaException ex, ServerWebExchange exchange) {
    log.error("Kafka exception occurred: {}", ExceptionLogEntryFactory.createFromThrowable(ex));

    ExceptionErrorBuilder exceptionErrorBuilder =
            ExceptionErrorBuilder.builder()
                    .withMessage("Kafka exception occurred")
                    .withHttpStatus(HttpStatus.BAD_GATEWAY)
                    .withServerWebExchange(exchange);

    if (Objects.nonNull(ex.getCause())) {
      Throwable cause = ex.getCause();
      List<ErrorDetailDto> errorDetailDto =
          List.of(
              new ErrorDetailDto(
                  String.valueOf(HttpStatus.BAD_GATEWAY.value()),
                  cause.getClass().getSimpleName(),
                  cause.getMessage()));
      exceptionErrorBuilder = exceptionErrorBuilder.withErrors(errorDetailDto);
    }
    return exceptionErrorBuilder.build();
  }

  @ExceptionHandler(Exception.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public ErrorDto handleException(Exception ex) {
    log.error(
        "Unexpected exception occurred: {}", ExceptionLogEntryFactory.createFromThrowable(ex));
    return ExceptionErrorBuilder.builder()
            .withThrowable(ex)
            .withHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR)
            .build();
  }
}
