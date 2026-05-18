package com.systemgrade.rtdr.domain.model;

import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.annotation.Id;

@RedisHash("threat_cache")
public class ThreatCache {
    @Id
    private String id;
}