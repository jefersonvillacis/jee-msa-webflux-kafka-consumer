package com.villacis.webflux.shared;

/**
 * @author Jeferson Villacis
 * @version 1.0
 * @since 10/08/2025 Copyright 2025
 */
public interface MessageJsonSerializer {
    String toJsonWithPrettyPrinter(Object exceptionLogEntry);
}
