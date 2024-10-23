package com.bueno.persistence.repositories;

import com.bueno.domain.usecases.tournament.dtos.MatchDTO;
import com.bueno.domain.usecases.tournament.dtos.TournamentDTO;
import com.bueno.domain.usecases.tournament.repos.MatchRepository;
import com.bueno.domain.usecases.utils.exceptions.EntityNotFoundException;
import com.bueno.persistence.dao.MatchDao;
import com.bueno.persistence.dao.TournamentDao;
import com.bueno.persistence.dto.MatchEntity;
import com.bueno.persistence.dto.TournamentEntity;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.bueno.persistence.repositories.TournamentRepositoryMongoImpl.getTournamentDTO;

@Repository
public class MatchesRepositoryMongoImpl implements MatchRepository {
    private final MatchDao matchDao;
    private final TournamentDao tournamentDao;

    public MatchesRepositoryMongoImpl(MatchDao matchDao, TournamentDao tournamentDao) {
        this.matchDao = matchDao;
        this.tournamentDao = tournamentDao;
    }

    @Override
    public Optional<MatchDTO> findById(UUID uuid) {
        Optional<MatchEntity> entity = matchDao.findMatchEntityByUuid(uuid);
        if (entity.isEmpty()) {
            throw new EntityNotFoundException("Match entity with uuid: " + uuid + " not found");
        }
        return toDtoFromEntity(entity.get());
    }

    @Override
    public List<MatchDTO> findAll() {
        List<MatchEntity> entityList = matchDao.findAll();
        return entityList.stream().map(entity -> toDtoFromEntity(entity).orElse(null)).toList();
    }

    @Override
    public List<MatchDTO> findMatchesByTournamentId(UUID tournamentUuid) {
        TournamentEntity entity = tournamentDao.findTournamentEntityByUuid(tournamentUuid).orElse(null);
        Optional<TournamentDTO> dto = getTournamentDTO(entity);
        List<MatchDTO> matchDTOS = new ArrayList<>();
        if (dto.isEmpty()) throw new EntityNotFoundException("Tournament not found");
        dto.get().matchUUIDs().forEach(mUuid ->
                matchDao.findMatchEntityByUuid(mUuid).ifPresent(m -> matchDTOS.add(m.toDto())));
        return matchDTOS;
    }

    @Override
    public void save(MatchDTO matchDTO) {
        //TODO - mudar tratamento de exceção aqui
        matchDao.save(MatchEntity.from(matchDTO));
    }

    @Override
    public void saveAll(List<MatchDTO> dtos) {
        List<MatchEntity> entityList = dtos.stream().map(MatchEntity::from).toList();
        matchDao.saveAll(entityList);
    }

    @Override
    public void update(MatchDTO matchDTO) {
        matchDao.deleteMatchEntityByUuid(matchDTO.uuid());
        matchDao.save(MatchEntity.from(matchDTO));
    }

    @Override
    public void updateAll(List<MatchDTO> matchDTOS) {
        List<UUID> uuidList = matchDTOS.stream().map(MatchDTO::uuid).toList();
        uuidList.forEach(matchDao::deleteMatchEntityByUuid);
        matchDao.saveAll(matchDTOS.stream().map(MatchEntity::from).toList());
    }

    @Override
    public void deleteAll() {
        matchDao.deleteAll();
    }

    private Optional<MatchDTO> toDtoFromEntity(MatchEntity entity) {

        return Optional.of(entity.toDto());
    }
}
