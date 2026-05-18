package com.systemgrade.rtdr.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.Map;

public record TelemetryRequest(

        @NotBlank(message = "Node identifier (hostname) is required")
        String hostname,

        @NotBlank(message = "Source IP of the threat is required")
        String sourceIp,

        @NotBlank(message = "Threat type is required")
        String threatType,

        @Min(0) @Max(100)
        @NotNull(message = "Initial risk impact score is required")
        Integer riskImpact,

        Map<String, Object> metadata
) {}