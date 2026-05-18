package com.systemgrade.rtdr.service.enforcement;

import com.systemgrade.rtdr.domain.model.Node;
import com.systemgrade.rtdr.domain.exception.SecuritySystemException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.time.Duration;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Service
@Slf4j
public class EnforcementService {

    private final ConcurrentMap<UUID, SseEmitter> emitters = new ConcurrentHashMap<>();
    private final WebClient.Builder webClientBuilder;

    public EnforcementService(WebClient.Builder webClientBuilder) {
        this.webClientBuilder = webClientBuilder;
    }

    public SseEmitter registerEmitter(UUID nodeId) {
        SseEmitter emitter = new SseEmitter(Duration.ofMinutes(30).toMillis());
        emitter.onCompletion(() -> emitters.remove(nodeId));
        emitter.onTimeout(() -> emitters.remove(nodeId));
        emitters.put(nodeId, emitter);
        return emitter;
    }

    public void unregisterEmitter(UUID nodeId) {
        SseEmitter e = emitters.remove(nodeId);
        if (e != null) {
            try { e.complete(); } catch (Exception ignored) {}
        }
    }

    public void pushEnforcementToNode(UUID nodeId, Map<String, Object> payload) {
        SseEmitter emitter = emitters.get(nodeId);
        if (emitter != null) {
            try {
                emitter.send(payload, MediaType.APPLICATION_JSON);
            } catch (IOException ex) {
                emitters.remove(nodeId);
                throw new SecuritySystemException.Enforcement("Failed to push SSE payload", ex);
            }
        } else {
            // If no SSE emitter, attempt HTTP callback if node provides URL in payload
            Object callback = payload.get("callback_url");
            if (callback instanceof String && !((String) callback).isBlank()) {
                try {
                    webClientBuilder.baseUrl((String) callback)
                            .build()
                            .post()
                            .contentType(MediaType.APPLICATION_JSON)
                            .bodyValue(payload)
                            .retrieve()
                            .bodyToMono(Void.class)
                            .doOnError(err -> log.warn("HTTP enforcement callback failed for node {}: {}", nodeId, err.getMessage()))
                            .subscribe();
                } catch (Exception ex) {
                    log.warn("HTTP enforcement callback failed for node {}: {}", nodeId, ex.getMessage());
                }
            } else {
                log.info("No active SSE emitter for node {} and no callback_url provided", nodeId);
            }
        }
    }
}
