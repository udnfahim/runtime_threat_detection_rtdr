package com.systemgrade.rtdr.service;

import com.systemgrade.rtdr.model.Node;
import com.systemgrade.rtdr.repository.NodeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class NodeService {

    private final NodeRepository nodeRepository;

    @Transactional
    public Node registerOrUpdate(String hostname, String ip, String os, String version) {
        return nodeRepository.findByHostname(hostname)
                .map(existingNode -> {
                    existingNode.setIpAddress(ip);
                    existingNode.setLastHeartbeat(OffsetDateTime.now());
                    existingNode.setStatus(Node.NodeStatus.ONLINE);
                    return nodeRepository.save(existingNode);
                })
                .orElseGet(() -> {
                    Node newNode = Node.builder()
                            .hostname(hostname)
                            .ipAddress(ip)
                            .osVersion(os)
                            .agentVersion(version)
                            .status(Node.NodeStatus.ONLINE)
                            .lastHeartbeat(OffsetDateTime.now())
                            .build();
                    return nodeRepository.save(newNode);
                });
    }

    @Transactional
    public void updateStatus(UUID nodeId, Node.NodeStatus status) {
        nodeRepository.findById(nodeId).ifPresent(node -> {
            node.setStatus(status);
            nodeRepository.save(node);
        });
    }
}