package com.bueno.domain.usecases.game.usecase;

import com.bueno.domain.usecases.game.repos.RankBotsRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class RankBotsOnTime {

    private final RankBotsUseCase rankBotsUseCase;
    private final RankBotsRepository rankBotsRepository;

    public RankBotsOnTime(RankBotsUseCase rankBotsUseCase, RankBotsRepository rankBotsRepository) {
        this.rankBotsUseCase = rankBotsUseCase;
        this.rankBotsRepository = rankBotsRepository;
    }

    @Scheduled(fixedRate = 1_800_000)
    public void updateRankTable() {
        rankBotsUseCase.rankAll();
        rankBotsRepository.refreshTable(rankBotsUseCase.getRank());
    }
}
