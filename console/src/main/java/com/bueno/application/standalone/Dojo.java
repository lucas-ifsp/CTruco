package com.bueno.application.standalone;

import com.bueno.domain.usecases.bot.providers.BotProviders;
import com.bueno.domain.usecases.game.PlayWithBotsUseCase;
import com.bueno.domain.usecases.game.dtos.CreateForBotsDto;
import com.bueno.domain.usecases.game.dtos.PlayWithBotsDto;
import com.bueno.domain.usecases.game.repos.GameRepoDisposableImpl;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Dojo{
    private static final UUID uuidBot1 = UUID.randomUUID();
    private static final UUID uuidBot2 = UUID.randomUUID();

    public static void main(String[] args) throws Exception {
        final var main = new Dojo();
        final var prompt = new UserPrompt();

        final var botNames = BotProviders.availableBots();
        final var numBots = botNames.size();
        final var times = 100; //To improve accuracy, increase the value of 'times'

        final var bot1 = botNames.indexOf("DummyBot"); //
        if (bot1 == -1) throw new Exception("Set a valid bot name");

        for (int bot2 = 0; bot2 < numBots; bot2++) {
            if (bot1 == bot2) continue;
            final var results = main.playManyInParallel(times, botNames.get(bot1), botNames.get(bot2));
            prompt.printResult(getResult(results));
        }

    }

    private List<PlayWithBotsDto> playManyInParallel(int times, String bot1Name, String bot2Name) {
        final Callable<PlayWithBotsDto> playGame = () -> play(bot1Name, bot2Name);
        return Stream.generate(() -> playGame)
                .limit(times)
                .parallel()
                .map(executeGameCall())
                .filter(Objects::nonNull)
                .toList();
    }

    private PlayWithBotsDto play(String bot1Name, String bot2Name){
        final var repo = new GameRepoDisposableImpl();
        final var useCase = new PlayWithBotsUseCase(repo);
        final var requestModel = new CreateForBotsDto(uuidBot1, bot1Name, uuidBot2, bot2Name);
        final var result = useCase.playWithBots(requestModel);
        return result;
    }

    private static Function<Callable<PlayWithBotsDto>, PlayWithBotsDto> executeGameCall() {
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

    private static LinkedHashMap<PlayWithBotsDto, Long> getResult(List<PlayWithBotsDto> result) {
        LinkedHashMap<PlayWithBotsDto, Long> map = new LinkedHashMap<>();
        result.stream()
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
                .forEach((bot, wins) -> map.put(bot, wins));
        return map;
    }


}
