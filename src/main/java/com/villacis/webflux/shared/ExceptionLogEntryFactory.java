package com.villacis.webflux.shared;

import com.villacis.webflux.domain.exception.AbstractRuntimeException;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import static java.util.Optional.ofNullable;
import static org.springframework.core.NestedExceptionUtils.getRootCause;

/**
 * @author Jeferson Villacis
 * @version 1.0
 * @since 08/07/2025 Copyright 2025
 */
public class ExceptionLogEntryFactory {
  private ExceptionLogEntryFactory() {
    throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
  }

  public static ExceptionLogEntry createFromThrowable(Throwable throwable) {
    StackTraceElement firstElement =
        throwable.getStackTrace().length > 0
            ? throwable.getStackTrace()[0]
            : new Throwable(throwable.getMessage()).getStackTrace()[0];

    String exceptionName = throwable.getClass().getSimpleName();

    return ExceptionLogEntry.builder()
        .date(LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))
        .exceptionName(exceptionName)
        .statusName(getStatusName(throwable))
        .statusCode(getStatusCode(throwable))
        .message(throwable.getMessage())
        .cause(ofNullable(getRootCause(throwable)).orElse(throwable).getMessage())
        .clazz(firstElement != null ? firstElement.getClassName() : "Unknown")
        .method(firstElement != null ? firstElement.getMethodName() : "Unknown")
        .line(firstElement != null ? firstElement.getLineNumber() : 0)
        .build();
  }

  public static String getStatusName(Throwable throwable) {
    return Optional.of(throwable)
        .filter(
            ex -> ex instanceof ResponseStatusException || ex instanceof AbstractRuntimeException)
        .map(
            ex ->
                ex instanceof ResponseStatusException exception
                    ? HttpStatus.valueOf((exception).getStatusCode().value()).name()
                    : HttpStatus.valueOf(((AbstractRuntimeException) ex).getStatus().value())
                        .name())
        .orElseGet(HttpStatus.BAD_REQUEST::name);
  }

  private static String getStatusCode(Throwable throwable) {
    return Optional.of(throwable)
        .filter(
            ex -> ex instanceof ResponseStatusException || ex instanceof AbstractRuntimeException)
        .map(
            ex ->
                ex instanceof ResponseStatusException exception
                    ? String.valueOf((exception).getStatusCode().value())
                    : String.valueOf(((AbstractRuntimeException) ex).getStatus().value()))
        .orElse(String.valueOf(HttpStatus.BAD_REQUEST.value()));
  }
}
