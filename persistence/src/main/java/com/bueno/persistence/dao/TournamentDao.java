package com.bueno.persistence.dao;

import com.bueno.persistence.dto.TournamentEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;
import java.util.UUID;

public interface TournamentDao extends MongoRepository<TournamentEntity, UUID> {
    Optional<TournamentEntity> findTournamentEntityByUuid(UUID uuid);

    void deleteTournamentEntityByUuid(UUID uuid);
}
