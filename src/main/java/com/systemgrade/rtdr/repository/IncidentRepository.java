package com.systemgrade.rtdr.repository;

import com.systemgrade.rtdr.model.Incident;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface IncidentRepository extends JpaRepository<Incident, UUID> {

    long countBySourceIpAndDetectedAtAfter(String sourceIp, OffsetDateTime detectedAt);

    List<Incident> findTop50ByNodeIdOrderByDetectedAtDesc(UUID nodeId);

    List<Incident> findBySeverity(Incident.Severity severity);
}