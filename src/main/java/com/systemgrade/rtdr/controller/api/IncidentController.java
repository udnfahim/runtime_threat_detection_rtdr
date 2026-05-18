package com.systemgrade.rtdr.controller.api;

import com.systemgrade.rtdr.domain.model.Incident;
import com.systemgrade.rtdr.domain.model.Policy;
import com.systemgrade.rtdr.dto.request.PolicyUpdateDTO;
import com.systemgrade.rtdr.dto.response.IncidentDetailsDTO;
import com.systemgrade.rtdr.repository.IncidentRepository;
import com.systemgrade.rtdr.repository.PolicyRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/incidents")
@RequiredArgsConstructor
public class IncidentController {

    private final IncidentRepository incidentRepository;
    private final PolicyRepository policyRepository;

    @GetMapping
    public ResponseEntity<List<IncidentDetailsDTO>> list() {
        var list = incidentRepository.findAll().stream().map(IncidentDetailsDTO::from).collect(Collectors.toList());
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<IncidentDetailsDTO> get(@PathVariable("id") UUID id) {
        return incidentRepository.findById(id).map(IncidentDetailsDTO::from).map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/policies")
    @Transactional
    public ResponseEntity<String> updatePolicy(@Valid @RequestBody PolicyUpdateDTO dto) {
        Policy p = Policy.builder()
                .name(dto.getName())
                .thresholds(dto.getThresholds())
                .autoBlockRules(dto.getAutoBlockRules())
                .escalationPaths(dto.getEscalationPaths())
                .build();
        policyRepository.save(p);
        return ResponseEntity.ok("policy_saved");
    }

    @PostMapping("/{id}/mitigate")
    public ResponseEntity<String> mitigate(@PathVariable("id") UUID id) {
        return incidentRepository.findById(id).map(incident -> {
            incident.setStatus(Incident.Status.MITIGATED);
            incident.setResolvedAt(java.time.Instant.now());
            incidentRepository.save(incident);
            return ResponseEntity.ok("mitigated");
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }
}
