package com.systemgrade.rtdr.domain.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;


import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "nodes", schema = "public")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Node {

    public enum Status { ONLINE, OFFLINE, DEGRADED, COMPROMISED }

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", columnDefinition = "uuid", nullable = false, updatable = false)
    private UUID id;

    @Column(name = "hostname", nullable = false, length = 128)
    private String hostname;

    @Column(name = "ip_address", nullable = false, length = 64)
    private String ipAddress;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 24)
    @Builder.Default
    private Status status = Status.OFFLINE;

    @Column(name = "agent_version", nullable = false, length = 64)
    @Builder.Default
    private String agentVersion = "0.0.0";

    @Column(name = "os_version", nullable = true, length = 64)
    @Builder.Default
    private String osVersion = "unknown";

    @Column(name = "last_heartbeat", columnDefinition = "timestamp with time zone")
    @Builder.Default
    private Instant lastHeartbeat = Instant.EPOCH;

    @PrePersist
    public void prePersist() {
        if (status == null) status = Status.OFFLINE;
        if (lastHeartbeat == null) lastHeartbeat = Instant.EPOCH;
    }
}
