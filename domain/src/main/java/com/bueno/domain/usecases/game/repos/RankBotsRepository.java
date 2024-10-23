package com.bueno.domain.usecases.game.repos;

import com.bueno.domain.usecases.game.dtos.BotRankInfoDto;
import org.springframework.stereotype.Repository;

import java.util.List;


public interface RankBotsRepository {
    List<BotRankInfoDto> findAll();

    void save(BotRankInfoDto botWinsOnRankDto);

    void saveAll(List<BotRankInfoDto> botWinsOnRankDto);

    void update(BotRankInfoDto botWinsOnRankDto);

//    void updateAll(List<BotRankInfoDto> botWinsOnRankDto);

    boolean refreshTable(List<BotRankInfoDto> botRankInfoDtos);

    boolean deleteByRank(BotRankInfoDto botInfo);
}
