package com.systemgrade.rtdr.domain.model;

import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.GenericGenerator;

import java.time.Instant;
import java.util.Collections;
import java.util.Map;
import java.util.UUID;

@Entity
@Table(name = "incidents", schema = "public")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Incident {

    public enum Severity { LOW, MEDIUM, HIGH, CRITICAL }
    public enum Status { ACTIVE, MITIGATED }

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", updatable = false, nullable = false, columnDefinition = "uuid")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "node_id")
    private Node node;

    @Column(name = "threat_type", nullable = false, length = 128)
    private String threatType;

    @Enumerated(EnumType.STRING)
    @Column(name = "severity", nullable = false, length = 16)
    private Severity severity;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 24)
    @Builder.Default
    private Status status = Status.ACTIVE;

    @Column(name = "risk_score", nullable = false)
    @Builder.Default
    private Integer riskScore = 0;

    @Column(name = "source_ip", length = 64)
    private String sourceIp;

    @Type(JsonType.class)
    @Column(name = "raw_payload", columnDefinition = "jsonb", nullable = false)
    @Builder.Default
    private Map<String, Object> rawPayload = Collections.emptyMap();

    @Column(name = "detected_at", columnDefinition = "timestamp with time zone")
    @Builder.Default
    private Instant detectedAt = Instant.now();

    @Column(name = "resolved_at", columnDefinition = "timestamp with time zone")
    private Instant resolvedAt;

    @PrePersist
    public void prePersist() {
        if (detectedAt == null) detectedAt = Instant.now();
        if (riskScore == null) riskScore = 0;
        if (status == null) status = Status.ACTIVE;
    }

    @PreUpdate
    public void preUpdate() { }
}
