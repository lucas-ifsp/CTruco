package com.bueno.persistence.dao;

import com.bueno.persistence.dto.MatchEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;
import java.util.UUID;

public interface MatchDao extends MongoRepository<MatchEntity, UUID> {
    Optional<MatchEntity> findMatchEntityByUuid(UUID uuid);

    void deleteMatchEntityByUuid(UUID uuid);
}
