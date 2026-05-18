package com.systemgrade.rtdr.domain.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.hibernate.annotations.GenericGenerator;

import java.time.Instant;
import java.util.Collections;
import java.util.Map;
import java.util.UUID;

@Entity
@Table(name = "policies", schema = "public")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Policy {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", updatable = false, nullable = false, columnDefinition = "uuid")
    private UUID id;

    @Column(name = "name", nullable = false, unique = true, length = 128)
    private String name;

    @Column(name = "rule_type", nullable = false, length = 64)
    private String ruleType;

    @Column(name = "threshold", nullable = false)
    @Builder.Default
    private Integer threshold = 0;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "thresholds", columnDefinition = "jsonb", nullable = false)
    @Builder.Default
    private Map<String, Object> thresholds = Collections.emptyMap();

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "auto_block_rules", columnDefinition = "jsonb", nullable = false)
    @Builder.Default
    private Map<String, Object> autoBlockRules = Collections.emptyMap();

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "escalation_paths", columnDefinition = "jsonb", nullable = false)
    @Builder.Default
    private Map<String, Object> escalationPaths = Collections.emptyMap();

    @Column(name = "is_active", nullable = false)
    @Builder.Default
    private Boolean isActive = Boolean.TRUE;

    @Column(name = "created_at", columnDefinition = "timestamp with time zone")
    @Builder.Default
    private Instant createdAt = Instant.now();

    @PrePersist
    public void prePersist() {
        if (createdAt == null) createdAt = Instant.now();
        if (isActive == null) isActive = Boolean.TRUE;
    }
}
