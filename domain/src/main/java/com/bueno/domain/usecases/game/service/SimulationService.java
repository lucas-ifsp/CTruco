package com.bueno.domain.usecases.game.service;

import com.bueno.domain.usecases.bot.providers.RemoteBotApi;
import com.bueno.domain.usecases.bot.repository.RemoteBotRepository;
import com.bueno.domain.usecases.bot.usecase.BotUseCase;
import com.bueno.domain.usecases.game.converter.GameConverter;
import com.bueno.domain.usecases.game.dtos.CreateForBotsDto;
import com.bueno.domain.usecases.game.dtos.PlayWithBotsDto;
import com.bueno.domain.usecases.game.repos.GameRepoDisposableImpl;
import com.bueno.domain.usecases.game.repos.GameRepository;
import com.bueno.domain.usecases.game.usecase.CreateGameUseCase;

import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.function.Function;
import java.util.stream.Stream;

public class SimulationService {
    private final RemoteBotRepository remoteBotRepository;
    private final RemoteBotApi remoteBotApi;

    public SimulationService(RemoteBotRepository remoteBotRepository, RemoteBotApi botApi) {
        this.remoteBotRepository = remoteBotRepository;
        this.remoteBotApi = botApi;
    }

    public List<PlayWithBotsDto> runInParallel(UUID uuidBotToEvaluate, String botToEvaluateName, String challengedBotName, int times ) {
        final Callable<PlayWithBotsDto> gameWaitingForBeCreatedAndPlayed =
                () -> simulate(uuidBotToEvaluate, botToEvaluateName, UUID.randomUUID(), challengedBotName);

        return Stream.generate(() -> gameWaitingForBeCreatedAndPlayed)
                .limit(times)
                .parallel()
                .map(executeGameCall())
                .filter(Objects::nonNull)
                .toList();
    }

    private PlayWithBotsDto simulate(UUID evaluatedUuid, String evaluateName, UUID challengedUuid, String challengedName) {
        GameRepository gameRepository = new GameRepoDisposableImpl();

        final var requestModel = new CreateForBotsDto(evaluatedUuid, evaluateName, challengedUuid, challengedName);
        final CreateGameUseCase createGameUseCase = new CreateGameUseCase(gameRepository, remoteBotRepository, remoteBotApi);
        createGameUseCase.createForBots(requestModel);
        final var game = gameRepository.findByPlayerUuid(requestModel.bot1Uuid()).map(GameConverter::fromDto).orElseThrow();
        final var botUseCase = new BotUseCase(gameRepository, remoteBotRepository, remoteBotApi);

        //Plays the game
        final var intel = botUseCase.playWhenNecessary(game);

        final var winnerUUID = intel.gameWinner().orElseThrow();
        final var winnerName = winnerUUID.equals(requestModel.bot1Uuid()) ?
                requestModel.bot1Name() : requestModel.bot2Name();
        return new PlayWithBotsDto(winnerUUID, winnerName);
    }


    private Function<Callable<PlayWithBotsDto>, PlayWithBotsDto> executeGameCall() {
        return gameCall -> {
            try {
                return gameCall.call();
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        };
    }
}
