package com.systemgrade.rtdr.dto;

import java.util.UUID;
import java.time.OffsetDateTime;

public record NodeResponse(
        UUID id,
        String hostname,
        String ipAddress,
        String status,
        OffsetDateTime lastHeartbeat
) {}