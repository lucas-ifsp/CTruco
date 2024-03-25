package com.bueno.application.withbots.features;

import com.bueno.domain.usecases.game.usecase.RankBotsUseCase;

import java.util.Map;

public class RankBots {

    public void allBots(){
        Map<String,Long> rankMap;
        RankBotsUseCase useCase = new RankBotsUseCase();
        rankMap = useCase.rankAll();
        System.out.println(rankMap);
    }
}
