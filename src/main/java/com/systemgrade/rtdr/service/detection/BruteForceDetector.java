package com.systemgrade.rtdr.service.detection;

import com.systemgrade.rtdr.domain.exception.SecuritySystemException;
import com.systemgrade.rtdr.dto.request.TelemetryIngestDTO;
import com.systemgrade.rtdr.domain.model.Policy;
import com.systemgrade.rtdr.repository.PolicyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class BruteForceDetector implements DetectionEngine {

    private final RedisTemplate<String, Object> redisTemplate;
    private final PolicyRepository policyRepository;
    private static final String KEY_PREFIX = "rtdr:bf:";

    @Override
    public DetectionResult analyze(TelemetryIngestDTO telemetry) {
        try {
            String key = buildKey(telemetry);
            Long count = incrementWithExpiry(key, telemetry.getWindowSeconds());
            Map<String, Object> thresholds = loadThresholds();
            int threshold = thresholds.getOrDefault("failed_logins_threshold", 10) instanceof Number
                    ? ((Number) thresholds.get("failed_logins_threshold")).intValue() : 10;
            if (count != null && count >= threshold) {
                return new DetectionResult(true, "brute_force_threshold_exceeded");
            }
            return new DetectionResult(false, "ok");
        } catch (DataAccessException ex) {
            throw new SecuritySystemException.DataAccess("Redis error during detection", ex);
        }
    }

    private String buildKey(TelemetryIngestDTO telemetry) {
        String subject = telemetry.getSubject() == null ? "unknown" : telemetry.getSubject();
        String ip = telemetry.getSourceIp() == null ? "unknown" : telemetry.getSourceIp();
        return KEY_PREFIX + subject + ":" + ip;
    }

    private Long incrementWithExpiry(String key, int windowSeconds) {
        Long count = redisTemplate.opsForValue().increment(key, 1L);
        if (count != null && count == 1L) {
            redisTemplate.expire(key, java.time.Duration.ofSeconds(windowSeconds));
        }
        return count;
    }

    @Transactional(readOnly = true)
    public Map<String, Object> loadThresholds() {
        return policyRepository.findAll().stream().findFirst().map(Policy::getThresholds).orElse(Map.of("failed_logins_threshold", 10));
    }
}
