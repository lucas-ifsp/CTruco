package com.bueno.persistence.dao;

import com.bueno.domain.usecases.tournament.dtos.TournamentDTO;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.UUID;

public interface TournamentDao extends MongoRepository<TournamentDTO, UUID> {
}
