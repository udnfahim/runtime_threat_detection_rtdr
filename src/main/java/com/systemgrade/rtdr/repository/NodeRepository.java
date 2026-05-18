package com.systemgrade.rtdr.repository;

import com.systemgrade.rtdr.domain.model.Node;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface NodeRepository extends JpaRepository<Node, UUID> {

    Optional<Node> findByHostname(String hostname);
    Optional<Node> findByIpAddress(String ipAddress);
    Optional<Node> findByHostnameAndIpAddress(String hostname, String ipAddress);
}
