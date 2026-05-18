package com.systemgrade.rtdr.service;

import org.springframework.stereotype.Service;
import java.util.Map;

@Service
public class RiskScoringEngine {

    public int calculateScore(int initialImpact, long frequency, Map<String, Object> metadata) {
        int score = initialImpact;

        if (frequency > 50) score += 40;
        else if (frequency > 10) score += 20;

        if (metadata.containsKey("failed_attempts")) {
            int attempts = (int) metadata.get("failed_attempts");
            score += (attempts * 5);
        }

        if (Boolean.TRUE.equals(metadata.get("is_vpn")) || Boolean.TRUE.equals(metadata.get("unknown_location"))) {
            score += 15;
        }

        return Math.min(score, 100);
    }

    public String classify(int score) {
        if (score >= 80) return "MALICIOUS";
        if (score >= 40) return "SUSPICIOUS";
        return "SAFE";
    }
}