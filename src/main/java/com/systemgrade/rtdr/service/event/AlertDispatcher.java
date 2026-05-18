package com.systemgrade.rtdr.service.event;

import com.systemgrade.rtdr.domain.model.Incident;
import com.systemgrade.rtdr.dto.response.AlertStreamDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AlertDispatcher {

    private final SimpMessagingTemplate messagingTemplate;
    private static final String TOPIC = "/topic/alerts";

    @Async
    public void dispatchAlert(Incident incident) {
        AlertStreamDTO dto = new AlertStreamDTO(
                incident.getId(),
                incident.getThreatType(),
                incident.getSeverity().name(),
                incident.getStatus().name(),
                incident.getRiskScore(),
                incident.getSourceIp(),
                incident.getRawPayload()
        );
        messagingTemplate.convertAndSend(TOPIC, dto);
    }
}
