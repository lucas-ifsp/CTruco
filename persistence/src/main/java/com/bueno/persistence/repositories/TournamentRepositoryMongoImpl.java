package com.bueno.persistence.repositories;

import com.bueno.domain.usecases.tournament.dtos.TournamentDTO;
import com.bueno.domain.usecases.tournament.repos.TournamentRepository;
import com.bueno.persistence.dao.TournamentDao;
import com.bueno.persistence.dto.TournamentEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityExistsException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class TournamentRepositoryMongoImpl implements TournamentRepository {
    private final TournamentDao tournamentDao;

    public TournamentRepositoryMongoImpl(TournamentDao tournamentDao) {
        this.tournamentDao = tournamentDao;
    }

    @Override
    public Optional<TournamentDTO> findTournamentById(UUID uuid) {
        TournamentEntity entity = tournamentDao.findTournamentEntityByUuid(uuid).orElse(null);
        return getTournamentDTO(entity);
    }


    @Override
    public List<TournamentDTO> findAll() {
        return tournamentDao.findAll().stream().map(entity -> getTournamentDTO(entity).orElse(null)).toList();
    }

    @Override
    public void save(TournamentDTO dto) {
        TournamentEntity entity = TournamentEntity.from(dto);
        tournamentDao.save(entity);
    }

    @Override
    public void update(TournamentDTO dto) {
        tournamentDao.deleteTournamentEntityByUuid(dto.uuid());
        if (tournamentDao.findTournamentEntityByUuid(dto.uuid()).isPresent()) {
            throw new EntityExistsException("Objeto não deletado");
        }
        TournamentEntity entity = TournamentEntity.from(dto);
        tournamentDao.save(entity);
    }

    @Override
    public void deleteAll() {
        tournamentDao.deleteAll();
    }

    static Optional<TournamentDTO> getTournamentDTO(TournamentEntity entity) {
        if (entity == null || entity.getUuid() == null) {
            return Optional.empty();
        }

        return Optional.of(entity.toDto());

    }

}
