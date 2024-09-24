package com.bueno.persistence.dao;

import com.bueno.domain.usecases.tournament.dtos.MatchDTO;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.UUID;

public interface TournamentMatchesDao extends MongoRepository<MatchDTO, UUID> {
}
