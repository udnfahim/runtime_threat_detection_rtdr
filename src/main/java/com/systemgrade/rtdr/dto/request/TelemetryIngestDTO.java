package com.systemgrade.rtdr.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.Collections;
import java.util.Map;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TelemetryIngestDTO {

    @NotBlank
    private String nodeId;

    private Instant timestamp;

    private String subject;

    private String sourceIp;

    @Builder.Default
    private Map<String, Object> payload = Collections.emptyMap();

    @Builder.Default
    private Integer failedLogins = 0;

    @Builder.Default
    private Integer requestRate = 0;

    @Builder.Default
    private Integer anomalyWeight = 0;

    @Builder.Default
    private Integer geoIrregularity = 0;

    @Builder.Default
    private Integer windowSeconds = 60;
}
