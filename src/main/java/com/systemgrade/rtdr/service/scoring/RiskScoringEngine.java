package com.systemgrade.rtdr.service.scoring;

import com.systemgrade.rtdr.domain.model.Incident;
import com.systemgrade.rtdr.domain.model.Node;
import com.systemgrade.rtdr.domain.model.Policy;
import com.systemgrade.rtdr.repository.IncidentRepository;
import com.systemgrade.rtdr.repository.PolicyRepository;
import com.systemgrade.rtdr.service.event.AlertDispatcher;
import com.systemgrade.rtdr.service.enforcement.EnforcementService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.time.Instant;
import java.util.Map;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@Component
@RequiredArgsConstructor
public class RiskScoringEngine {

    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    private final IncidentRepository incidentRepository;
    private final AlertDispatcher alertDispatcher;
    private final EnforcementService enforcementService;
    private final PolicyRepository policyRepository;
    private final PlatformTransactionManager txManager;

    public int calculateRisk(Map<String, Number> metrics) {
        lock.readLock().lock();
        try {
            int failedLogins = getInt(metrics, "failed_logins");
            int requestRate = getInt(metrics, "request_rate");
            int anomalyWeight = getInt(metrics, "anomaly_weight");
            int geoIrregularity = getInt(metrics, "geo_irregularity");
            return failedLogins + requestRate + anomalyWeight + geoIrregularity;
        } finally {
            lock.readLock().unlock();
        }
    }

    private int getInt(Map<String, Number> m, String key) {
        Number n = m.get(key);
        return n == null ? 0 : n.intValue();
    }

    /**
     * Evaluate and persist incident if risk meets configured malicious threshold.
     */
    public void evaluateAndAct(Node node, String threatType, Map<String, Number> metrics, Map<String, Object> rawPayload, String sourceIp) {
        int risk = calculateRisk(metrics);

        int maliciousThreshold = policyRepository.findAll().stream().findFirst()
                .map(Policy::getThresholds)
                .map(th -> th.getOrDefault("malicious_threshold", 20))
                .map(Object::toString)
                .map(Integer::parseInt)
                .orElse(20);

        if (risk >= maliciousThreshold) {
            // Persist incident within transaction
            DefaultTransactionDefinition def = new DefaultTransactionDefinition();
            TransactionStatus status = txManager.getTransaction(def);
            try {
                Incident incident = Incident.builder()
                        .node(node)
                        .threatType(threatType)
                        .severity(risk >= 40 ? Incident.Severity.CRITICAL : Incident.Severity.HIGH)
                        .status(Incident.Status.ACTIVE)
                        .riskScore(risk)
                        .sourceIp(sourceIp)
                        .rawPayload(rawPayload)
                        .detectedAt(Instant.now())
                        .build();
                Incident saved = incidentRepository.save(incident);
                txManager.commit(status);
                // Asynchronously dispatch
                alertDispatcher.dispatchAlert(saved);
                // Push enforcement payload down node SSE if connected
                enforcementService.pushEnforcementToNode(node.getId(), Map.of(
                        "incidentId", saved.getId().toString(),
                        "tsid", saved.getId().toString(),
                        "rule", "auto_block",
                        "raw_payload", saved.getRawPayload()
                ));
            } catch (Exception ex) {
                txManager.rollback(status);
                throw ex;
            }
        }
    }
}
