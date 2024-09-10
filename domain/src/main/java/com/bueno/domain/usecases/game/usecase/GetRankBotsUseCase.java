package com.bueno.domain.usecases.game.usecase;

import com.bueno.domain.usecases.game.dtos.BotRankInfoDto;
import com.bueno.domain.usecases.game.repos.RankBotsRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GetRankBotsUseCase {
    private final RankBotsRepository rankBotsRepository;
    private List<BotRankInfoDto> rank;


    public GetRankBotsUseCase(RankBotsRepository rankBotsRepository) {
        this.rankBotsRepository = rankBotsRepository;
    }

    public List<BotRankInfoDto> exec() {
        rank = rankBotsRepository.findAll();

        return rank;
    }

    public int getNumberOfBots() {
        return rank.size();
    }

}
