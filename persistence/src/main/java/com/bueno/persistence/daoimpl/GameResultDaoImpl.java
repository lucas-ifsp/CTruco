package com.bueno.persistence.daoimpl;

import com.bueno.persistence.dao.GameResultDao;
import com.bueno.persistence.dto.GameResultEntity;
import com.bueno.persistence.dto.GameResultQR;
import com.bueno.persistence.dto.PlayerWinsQR;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public class GameResultDaoImpl implements GameResultDao {
    @Override
    public List<PlayerWinsQR> findTopWinners(Pageable pageable) {
        return List.of();
    }

    @Override
    public List<GameResultQR> findAllByPlayerUuid(UUID uuid) {
        return List.of();
    }

    @Override
    public void save(GameResultEntity game) {

    }
}
