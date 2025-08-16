package com.villacis.webflux.domain.model;

import java.time.LocalDateTime;

/**
 * @author Jeferson Villacis
 * @version 1.0
 * @since 09/08/2025 Copyright 2025
 */
public record MessageDto(LocalDateTime createdAt, String topic, String headers, String message) {}
