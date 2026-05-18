package com.systemgrade.rtdr.controller.api;

import com.systemgrade.rtdr.domain.model.Node;
import com.systemgrade.rtdr.dto.request.TelemetryIngestDTO;
import com.systemgrade.rtdr.repository.IncidentRepository;
import com.systemgrade.rtdr.repository.NodeRepository;
import com.systemgrade.rtdr.service.detection.BruteForceDetector;
import com.systemgrade.rtdr.service.enforcement.EnforcementService;
import com.systemgrade.rtdr.service.scoring.RiskScoringEngine;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/telemetry")
@RequiredArgsConstructor
public class TelemetryController {

    private final BruteForceDetector bruteForceDetector;
    private final RiskScoringEngine riskScoringEngine;
    private final EnforcementService enforcementService;
    private final IncidentRepository incidentRepository;
    private final NodeRepository nodeRepository;

    @PostMapping(consumes = "application/json")
    public ResponseEntity<String> ingest(@RequestHeader("X-Agent-Token") String token, @Valid @RequestBody TelemetryIngestDTO telemetry) {
        var detection = bruteForceDetector.analyze(telemetry);

        Map<String, Number> metrics = Map.of(
                "failed_logins", telemetry.getFailedLogins(),
                "request_rate", telemetry.getRequestRate(),
                "anomaly_weight", telemetry.getAnomalyWeight(),
                "geo_irregularity", telemetry.getGeoIrregularity()
        );

        Node node = null;
        try {
            node = nodeRepository.findById(UUID.fromString(telemetry.getNodeId())).orElse(null);
        } catch (Exception ignored) {}

        riskScoringEngine.evaluateAndAct(node, detection.getReason(), metrics, Map.of("telemetry", telemetry), telemetry.getSourceIp());

        return ResponseEntity.status(HttpStatus.ACCEPTED).body("ingested");
    }

    @GetMapping(path = "/enforcement/stream/{nodeId}", produces = "text/event-stream")
    public SseEmitter stream(@PathVariable("nodeId") UUID nodeId, @RequestHeader("X-Agent-Token") String token) {
        return enforcementService.registerEmitter(nodeId);
    }
}
