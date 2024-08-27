package com.bueno.controllers;

import com.bueno.domain.usecases.bot.providers.BotManagerService;
import com.bueno.domain.usecases.bot.providers.RemoteBotApi;
import com.bueno.domain.usecases.bot.repository.RemoteBotRepository;
import com.bueno.domain.usecases.game.usecase.EvaluateBotsUseCase;
import com.bueno.domain.usecases.game.usecase.RankBotsUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/bots")
public class BotController {
    BotManagerService provider;
    RemoteBotRepository remoteBotRepository;
    RemoteBotApi remoteBotApi;
    RankBotsUseCase rankUseCase;

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
    private ResponseEntity<?> EvaluateBot(@PathVariable String botName) {
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
    private ResponseEntity<?> RankBots() {
        try {
            if (rankUseCase.isRanking() && !rankUseCase.hasRank())
                return ResponseEntity.ok("Ranking...");
            else {
                if (rankUseCase.isRanking() && rankUseCase.hasRank()) {
                    return ResponseEntity.ok(rankUseCase.getRank());
                } else {
                    rankUseCase.rankAll();
                    return ResponseEntity.ok("Ranking...");
                }
            }
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

}
