package com.villacis.webflux.domain.model;

import java.util.List;

/**
 * @author Jeferson Villacis
 * @version 1.0
 * @since 09/08/2025 Copyright 2025
 */
public record ErrorDto(
    String title,
    String message,
    String instance,
    String type,
    String resource,
    String component,
    List<ErrorDetailDto> errors) {}
