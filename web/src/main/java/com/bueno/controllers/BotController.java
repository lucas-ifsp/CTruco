package com.bueno.controllers;

import com.bueno.domain.usecases.bot.providers.BotManagerService;
import com.bueno.domain.usecases.bot.providers.RemoteBotApi;
import com.bueno.domain.usecases.bot.repository.RemoteBotRepository;
import com.bueno.domain.usecases.game.dtos.BotRankInfoDto;
import com.bueno.domain.usecases.game.dtos.RankBotsResponse;
import com.bueno.domain.usecases.game.usecase.*;
import com.bueno.responses.ResponseBuilder;
import com.bueno.responses.ResponseEntry;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/bots")
public class BotController {
    private final BotManagerService provider;
    private final RemoteBotRepository remoteBotRepository;
    private final RemoteBotApi remoteBotApi;
    private final GetRankBotsUseCase getRankUseCase;
    private final EvaluateBotsUseCase evaluateUseCase;
    private final Thread rankInParallelThread;

    public BotController(BotManagerService provider,
                         RemoteBotRepository remoteBotRepository,
                         RemoteBotApi remoteBotApi,
                         GetRankBotsUseCase getRankUseCase,
                         RankAllInParallelUseCase rankInParallel,
                         EvaluateBotsUseCase evaluateUseCase) {
        this.provider = provider;
        this.remoteBotRepository = remoteBotRepository;
        this.remoteBotApi = remoteBotApi;
        this.getRankUseCase = getRankUseCase;
        this.rankInParallelThread = new Thread(rankInParallel);
        this.evaluateUseCase = evaluateUseCase;
    }

    @GetMapping
    private List<String> getBotNames() {
        return provider.providersNames();
    }

    //TODO - fazer endpoint Evaluate Bots
    @PostMapping("/evaluate/{botName}")
    private ResponseEntity<?> evaluateBot(@PathVariable String botName) {
        try {
            System.out.println("avaliando bot");
            var evaluateResults = evaluateUseCase.evaluateWithAll(botName);
            System.out.println("terminou");
            return new ResponseBuilder(HttpStatus.OK)
                    .addEntry(new ResponseEntry("payload", evaluateResults))
                    .addTimestamp()
                    .build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @PostMapping("/rank")
    private ResponseEntity<?> rankBots() {
        try {
            if (rankInParallelThread.isAlive()) throw new InterruptedException("Thread is already running");
//            rankInParallelThread.interrupt();
//            rankInParallelThread = new Thread(rankInParallel);
            rankInParallelThread.start();
            return new ResponseBuilder(HttpStatus.OK)
                    .addEntry(new ResponseEntry("request accepted", "starting to rank"))
                    .addTimestamp()
                    .build();
        } catch (InterruptedException e) {
            return new ResponseBuilder(HttpStatus.CONFLICT)
                    .addEntry(new ResponseEntry("error", e.getMessage()))
                    .addTimestamp()
                    .build();
        } catch (Exception e) {
            return new ResponseBuilder(HttpStatus.NOT_FOUND)
                    .addEntry(new ResponseEntry("error", "something went wrong"))
                    .addTimestamp()
                    .build();
        }
    }

    @GetMapping("/rank")
    private ResponseEntity<?> getRankFromDB() {
        try {
            List<BotRankInfoDto> results = getRankUseCase.exec();
            RankBotsResponse response = new RankBotsResponse(results,
                    getNumberOfSimulationsToRankOneBot(getRankUseCase.getNumberOfBots()));
            return new ResponseBuilder(HttpStatus.OK)
                    .addEntry(new ResponseEntry("payload", response))
                    .addTimestamp()
                    .build();
        } catch (Exception e) {
            return new ResponseBuilder(HttpStatus.NOT_FOUND)
                    .addEntry(new ResponseEntry("error", "the server couldn't found bots rank"))
                    .addTimestamp()
                    .build();
        }
    }

    public static long getNumberOfSimulationsToRankOneBot(int numberOfBots) {
        return (long) (numberOfBots - 1) * RankBotsUseCase.TIMES_RANK;
    }
}
