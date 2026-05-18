package com.systemgrade.rtdr.dto.response;

import com.systemgrade.rtdr.domain.model.Incident;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class IncidentDetailsDTO {

    private UUID id;
    private String threatType;
    private Incident.Severity severity;
    private Incident.Status status;
    private Integer riskScore;
    private String sourceIp;
    private Map<String, Object> rawPayload;
    private Instant detectedAt;
    private Instant resolvedAt;

    public static IncidentDetailsDTO from(Incident incident) {
        return new IncidentDetailsDTO(
                incident.getId(),
                incident.getThreatType(),
                incident.getSeverity(),
                incident.getStatus(),
                incident.getRiskScore(),
                incident.getSourceIp(),
                incident.getRawPayload(),
                incident.getDetectedAt(),
                incident.getResolvedAt()
        );
    }
}
