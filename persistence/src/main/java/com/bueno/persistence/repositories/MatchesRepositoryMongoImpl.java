package com.bueno.persistence.repositories;

import com.bueno.domain.usecases.tournament.dtos.MatchDTO;
import com.bueno.domain.usecases.tournament.repos.MatchRepository;
import com.bueno.persistence.dao.MatchDao;
import com.bueno.persistence.dto.MatchEntity;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class MatchesRepositoryMongoImpl implements MatchRepository {
    private final MatchDao dao;

    public MatchesRepositoryMongoImpl(MatchDao dao) {
        this.dao = dao;
    }

    @Override
    public Optional<MatchDTO> findById(UUID uuid) {
        MatchEntity entity = dao.findMatchEntityByUuid(uuid).orElse(null);
        return getMatchDtoFrom(entity);
    }

    @Override
    public List<MatchDTO> findAll() {
        List<MatchEntity> entityList = dao.findAll();
        return entityList.stream().map(entity -> getMatchDtoFrom(entity).orElse(null)).toList();
    }


    @Override
    public void save(MatchDTO matchDTO) {

    }

    private Optional<MatchDTO> getMatchDtoFrom(MatchEntity entity) {
        if (entity == null || entity.getUuid() == null) {
            return Optional.empty();
        }
        return Optional.of(new MatchDTO(entity.getUuid(),
                entity.getMatchNumber(),
                entity.getP1Name(),
                entity.getP2Name(),
                entity.isAvailable(),
                entity.getWinnerName(),
                entity.getP1Score(),
                entity.getP2Score(),
                entity.getNext())
        );
    }
}
