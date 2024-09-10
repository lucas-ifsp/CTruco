package com.bueno.domain.usecases.game.usecase;

import com.bueno.domain.usecases.game.dtos.BotRankInfoDto;
import com.bueno.domain.usecases.game.repos.RankBotsRepository;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class RankAllInParallelUseCase implements Runnable {
    private final RankBotsUseCase rankBotsUseCase;
    private List<BotRankInfoDto> rank;
    private final RankBotsRepository rankBotsRepository;


    public RankAllInParallelUseCase(RankBotsUseCase rankBotsUseCase, RankBotsRepository rankBotsRepository) {
        this.rankBotsUseCase = rankBotsUseCase;
        this.rankBotsRepository = rankBotsRepository;
        this.rank = new ArrayList<>();
    }

    @Override
    public void run() {
        rankBotsUseCase.rankAll();
        rank = rankBotsUseCase.getRank();
        System.out.println("Vai Atualizar no banco agora");
        rank.forEach(System.out::println);
        rankBotsRepository.refreshTable(rank);
    }


}
