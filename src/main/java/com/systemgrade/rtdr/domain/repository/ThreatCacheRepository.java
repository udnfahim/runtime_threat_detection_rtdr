package com.systemgrade.rtdr.domain.repository;

import com.systemgrade.rtdr.domain.model.ThreatCache;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ThreatCacheRepository extends CrudRepository<ThreatCache, String> {
    Optional<ThreatCache> findById(String id);
}