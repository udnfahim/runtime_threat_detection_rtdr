package com.systemgrade.rtdr.controller;

import com.systemgrade.rtdr.model.Node;
import com.systemgrade.rtdr.service.NodeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/nodes")
@RequiredArgsConstructor
public class NodeController {

    private final NodeService nodeService;

    @PostMapping("/register")
    public ResponseEntity<Node> register(
            @RequestParam String hostname,
            @RequestParam String ip,
            @RequestParam String os,
            @RequestParam String version) {

        Node node = nodeService.registerOrUpdate(hostname, ip, os, version);
        return ResponseEntity.ok(node);
    }
}