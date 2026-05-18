package com.systemgrade.rtdr.service;

import com.systemgrade.rtdr.domain.model.Incident;
import com.systemgrade.rtdr.dto.request.TelemetryIngestDTO;
import com.systemgrade.rtdr.repository.IncidentRepository;
import com.systemgrade.rtdr.service.detection.BruteForceDetector;
import com.systemgrade.rtdr.service.enforcement.EnforcementService;
import com.systemgrade.rtdr.service.event.AlertDispatcher;
import com.systemgrade.rtdr.service.scoring.RiskScoringEngine;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class TelemetryService {

    private final BruteForceDetector bruteForceDetector;
    private final RiskScoringEngine riskScoringEngine;
    private final IncidentRepository incidentRepository;
    private final AlertDispatcher alertDispatcher;
    private final EnforcementService enforcementService;

    @Transactional
    public void processTelemetry(TelemetryIngestDTO telemetry) {
        var detection = bruteForceDetector.analyze(telemetry);

        Map<String, Number> metrics = Map.of(
                "failed_logins", telemetry.getFailedLogins(),
                "request_rate", telemetry.getRequestRate(),
                "anomaly_weight", telemetry.getAnomalyWeight(),
                "geo_irregularity", telemetry.getGeoIrregularity()
        );

        int risk = riskScoringEngine.calculateRisk(metrics);

        if (risk >= 0) {
            if (risk >= 20 || detection.isSuspicious()) {
                Incident incident = Incident.builder()
                        .node(null)
                        .threatType(detection.getReason())
                        .severity(risk >= 40 ? Incident.Severity.CRITICAL : Incident.Severity.HIGH)
                        .status(Incident.Status.ACTIVE)
                        .riskScore(risk)
                        .sourceIp(telemetry.getSourceIp())
                        .rawPayload(Map.of("telemetry", telemetry))
                        .detectedAt(Instant.now())
                        .build();

                Incident saved = incidentRepository.save(incident);
                alertDispatcher.dispatchAlert(saved);
                try {
                    if (telemetry.getNodeId() != null && !telemetry.getNodeId().isBlank()) {
                        enforcementService.pushEnforcementToNode(java.util.UUID.fromString(telemetry.getNodeId()), Map.of(
                                "incidentId", saved.getId().toString(),
                                "rule", "auto_block",
                                "raw_payload", saved.getRawPayload()
                        ));
                    }
                } catch (Exception ex) {
                    log.warn("Enforcement push failed: {}", ex.getMessage());
                }
            }
        }
    }
}

