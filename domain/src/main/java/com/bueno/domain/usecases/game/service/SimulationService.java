package com.bueno.domain.usecases.game.service;

import com.bueno.domain.usecases.bot.BotUseCase;
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
   private final UUID uuidBot1;
   private final String bot1Name;
   private final UUID uuidBot2;
   private final String bot2Name;

    public SimulationService(UUID uuidBotToEvaluate, String botToEvaluateName, String challengedBotName) {
        this.uuidBot1 = uuidBotToEvaluate;
        this.bot1Name = botToEvaluateName;
        this.uuidBot2 = UUID.randomUUID();;
        this.bot2Name = challengedBotName;
    }

    public List<PlayWithBotsDto> runInParallel(int times) {
        final Callable<PlayWithBotsDto> gameWaitingForBeCreatedAndPlayed = this::simulate;
        return Stream.generate(() -> gameWaitingForBeCreatedAndPlayed)
                .limit(times)
                .parallel()
                .map(executeGameCall())
                .filter(Objects::nonNull)
                .toList();
    }

    private PlayWithBotsDto simulate(){
        GameRepository gameRepository = new GameRepoDisposableImpl();
        final var requestModel = new CreateForBotsDto(uuidBot1, bot1Name, uuidBot2, bot2Name);
        final CreateGameUseCase createGameUseCase = new CreateGameUseCase(gameRepository);
        createGameUseCase.createForBots(requestModel);
        final var game = gameRepository.findByPlayerUuid(requestModel.bot1Uuid()).map(GameConverter::fromDto).orElseThrow();
        final var botUseCase = new BotUseCase(gameRepository);

        //Plays the game
        final var intel = botUseCase.playWhenNecessary(game);

        final var winnerUUID = intel.gameWinner().orElseThrow();
        final var winnerName = winnerUUID.equals(requestModel.bot1Uuid()) ?
                requestModel.bot1Name() : requestModel.bot2Name();
//        System.out.println("Winner: " + winnerName);
        return new PlayWithBotsDto(winnerUUID, winnerName);
    }


    private Function<Callable<PlayWithBotsDto>, PlayWithBotsDto> executeGameCall(){
        return gameCall -> {
            try {
                return gameCall.call();
            }
            catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        };
    }
}
