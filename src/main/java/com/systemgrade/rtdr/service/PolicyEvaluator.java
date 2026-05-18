package com.systemgrade.rtdr.service;

import com.systemgrade.rtdr.model.Incident;
import com.systemgrade.rtdr.model.Policy;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PolicyEvaluator {

    public boolean shouldBlock(Incident incident, List<Policy> activePolicies) {
        if (incident == null || activePolicies == null) return false;

        return activePolicies.stream()
                .filter(p -> p.getRuleType() != null &&
                        p.getRuleType().equalsIgnoreCase(incident.getThreatType()))
                .anyMatch(p -> incident.getRiskScore() >= p.getThreshold());
    }
}