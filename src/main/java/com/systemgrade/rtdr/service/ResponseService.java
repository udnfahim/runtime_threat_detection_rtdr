package com.systemgrade.rtdr.service;


import com.systemgrade.rtdr.domain.model.Incident;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class ResponseService {

    public void executeMitigation(Incident incident) {
        log.warn("MITIGATION TRIGGERED: Blocking IP {} due to high risk score: {}",
                incident.getSourceIp(), incident.getRiskScore());

        String nodeHostname = incident.getNode().getHostname();
        log.info("Sending 'BLOCK' command to agent on node: {}", nodeHostname);
    }
}