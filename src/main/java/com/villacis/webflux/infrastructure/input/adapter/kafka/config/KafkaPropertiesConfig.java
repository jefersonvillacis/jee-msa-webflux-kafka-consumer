package com.villacis.webflux.infrastructure.input.adapter.kafka.config;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * @author Jeferson Villacis
 * @version 1.0
 * @since 09/08/2025 Copyright 2025
 */
@Getter
@Setter
@Configuration
@NoArgsConstructor
@ConfigurationProperties(prefix = "spring.kafka")
public class KafkaPropertiesConfig {
    private String bootstrapServers;
    private Consumer consumer;
    private List<String> allowedHeaders;
    private Topic topic;
    private String timezone;

    @Getter
    @Setter
    public static class Consumer {
        private String groupId;
        private String autoOffsetReset;
        private String keyDeserializer;
        private String valueDeserializer;
    }

    @Getter
    @Setter
    public static class Topic {
        private String name;
    }
}
