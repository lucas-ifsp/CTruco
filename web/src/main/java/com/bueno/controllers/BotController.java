package com.bueno.controllers;

import com.bueno.domain.usecases.bot.providers.BotManagerService;
import com.bueno.domain.usecases.bot.providers.RemoteBotApi;
import com.bueno.domain.usecases.bot.repository.RemoteBotRepository;
import com.bueno.domain.usecases.game.dtos.RankBotsResponse;
import com.bueno.domain.usecases.game.repos.GameResultRepository;
import com.bueno.domain.usecases.game.usecase.EvaluateBotsUseCase;
import com.bueno.domain.usecases.game.usecase.RankBotsUseCase;
import com.bueno.domain.usecases.game.usecase.ReportTopWinnersUseCase;
import com.bueno.persistence.repositories.GameResultRepositoryImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/bots")
public class BotController {
    private final BotManagerService provider;
    private final RemoteBotRepository remoteBotRepository;
    private final RemoteBotApi remoteBotApi;
    private final RankBotsUseCase rankUseCase;


    public BotController(BotManagerService provider,
                         RemoteBotRepository remoteBotRepository,
                         RemoteBotApi remoteBotApi
    ) {
        this.provider = provider;
        this.remoteBotRepository = remoteBotRepository;
        this.remoteBotApi = remoteBotApi;
        this.rankUseCase = new RankBotsUseCase(remoteBotRepository, remoteBotApi);
    }

    @GetMapping
    private List<String> getBotNames() {
        return provider.providersNames();
    }

    //TODO - fazer endpoint Evaluate Bots
    @PostMapping("/{botName}")
    private ResponseEntity<?> evaluateBot(@PathVariable String botName) {
        try {
            EvaluateBotsUseCase evaluateUseCase = new EvaluateBotsUseCase(remoteBotRepository, remoteBotApi, provider);
            var evaluateResults = evaluateUseCase.evaluateWithAll(botName);
            return ResponseEntity.ok().body(evaluateResults);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    //TODO - fazer endpoint Rank Bots -> hall da fama
    @PostMapping("/rank")
    private ResponseEntity<?> rankBots() {
        try {
            var response = rankBotsResponseFactory();
            return ResponseEntity.ok().body(response);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }


    private RankBotsResponse rankBotsResponseFactory() {
        if (rankUseCase.isRanking())
            return new RankBotsResponse(rankUseCase.isRanking(), rankUseCase.getRank(), "Ranking... " + rankUseCase.getProcessingTime() + "ms");
        if (rankUseCase.hasRank()) {
            rankUseCase.setHasRank(false);
            return new RankBotsResponse(rankUseCase.isRanking(), rankUseCase.getRank(), "Rank finished");
        }
        rankUseCase.rankAll();
        return new RankBotsResponse(rankUseCase.isRanking(), rankUseCase.getRank(), "Starting to rank");

    }

}
