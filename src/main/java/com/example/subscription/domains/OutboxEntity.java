package com.example.subscription.domains;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.JsonNode;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

/**
 * @author Sahand Jalilvand 16.01.24
 */
@Entity
@Table(name = "TBL_OUTBOX")
@EntityListeners(AuditingEntityListener.class)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@NamedNativeQuery(name = OutboxEntity.INSERT,
        query = "INSERT INTO tbl_outbox (idempotent_key, created_at, correlation_id, domain, event_type, message_data, span_id, trace_id)\n" +
                "VALUES (:idempotentKey, :createdAt, :correlationId, :domain, :eventType, :messageData, :spanId, :traceId)", resultClass = Void.class)
public class OutboxEntity implements Serializable {

    public static final String INSERT = "OutboxEntity_insert";

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "idempotent_key")
    private UUID idempotentKey;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private Date createdAt;

    @Column(name = "correlation_id", nullable = false)
    private String correlationId;

    @Column(name = "domain", nullable = false)
    private String domain;

    @Column(name = "event_type", nullable = false)
    private String eventType;

    @JsonIgnore
    @Column(name = "message_data", nullable = false, columnDefinition = "TEXT")
    private String messageData;

    @JsonIgnore
    @Column(name = "span_id", nullable = false)
    private String spanId;

    @JsonIgnore
    @Column(name = "trace_id", nullable = false)
    private String traceId;

    @Transient
    private JsonNode payload;
}
