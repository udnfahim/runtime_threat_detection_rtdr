package com.systemgrade.rtdr.service.detection;

import com.systemgrade.rtdr.dto.request.TelemetryIngestDTO;

public interface DetectionEngine {
    DetectionResult analyze(TelemetryIngestDTO telemetry);

    class DetectionResult {
        private final boolean suspicious;
        private final String reason;

        public DetectionResult(boolean suspicious, String reason) {
            this.suspicious = suspicious;
            this.reason = reason;
        }

        public boolean isSuspicious() { return suspicious; }
        public String getReason() { return reason; }
    }
}
