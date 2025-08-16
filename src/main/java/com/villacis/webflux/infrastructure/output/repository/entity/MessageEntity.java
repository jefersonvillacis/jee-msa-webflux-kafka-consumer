package com.villacis.webflux.infrastructure.output.repository.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import java.time.LocalDateTime;

/**
 * @author Jeferson Villacis
 * @version 1.0
 * @since 09/08/2025 Copyright 2025
 */
@Getter
@Setter
@Builder
@Table("MSG_MENSAJE")
public class MessageEntity {
    @Id
    private Long id;

    @Column("FECHA_REGISTRO")
    private LocalDateTime createdAt;

    @Column("TOPICO")
    private String topic;

    @Column("CABECERAS")
    private String headers;

    @Column("MENSAJE")
    private String message;
}
