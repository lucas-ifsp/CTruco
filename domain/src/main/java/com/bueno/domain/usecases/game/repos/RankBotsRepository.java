package com.bueno.domain.usecases.game.repos;

import com.bueno.domain.usecases.game.dtos.BotRankInfoDto;

import java.util.List;

public interface RankBotsRepository {
    List<BotRankInfoDto> findAll();

    void save(BotRankInfoDto botRankInfoDto);

    void saveAll(List<BotRankInfoDto> botRankInfoDto);

    void update(BotRankInfoDto botRankInfoDto);

    void updateAll(List<BotRankInfoDto> botRankInfoDto);
}
