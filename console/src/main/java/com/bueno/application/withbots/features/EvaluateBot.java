package com.bueno.application.withbots.features;

import com.bueno.application.withbots.commands.BotsAvailablePrinter;
import com.bueno.application.withbots.commands.BotOptionReader;
import com.bueno.application.withbots.commands.EvaluateBotsPrinter;
import com.bueno.application.withbots.commands.WaitingMessagePrinter;
import com.bueno.domain.usecases.bot.providers.BotManagerService;
import com.bueno.domain.usecases.bot.providers.RemoteBotApi;
import com.bueno.domain.usecases.bot.repository.RemoteBotRepository;
import com.bueno.domain.usecases.game.dtos.EvaluateResultsDto;
import com.bueno.domain.usecases.game.usecase.EvaluateBotsUseCase;
import com.bueno.persistence.repositories.RemoteBotRepositoryFileImpl;
import com.remote.RemoteBotApiAdapter;

import java.util.List;

public class EvaluateBot {

    private final RemoteBotRepository repository;
    private final RemoteBotApi botApi;

    public EvaluateBot() {
        repository = new RemoteBotRepositoryFileImpl();
        botApi = new RemoteBotApiAdapter();
    }

    public void againstAll() {
        RemoteBotRepository repository = new RemoteBotRepositoryFileImpl();
        RemoteBotApi botApi = new RemoteBotApiAdapter();
        BotManagerService providerService = new BotManagerService(repository, botApi);
        final var botNames = providerService.providersNames();

        printAvailableBots(botNames);
        String botToEvaluateName = botNames.get(scanBotOption(botNames) - 1);

        printWaitingMessage();

        printResultEvaluateBot(getEvaluateResultsDto(botToEvaluateName, botNames), botToEvaluateName);
    }

    private EvaluateResultsDto getEvaluateResultsDto(String botToEvaluateName, List<String> botNames) {
        EvaluateBotsUseCase useCase = new EvaluateBotsUseCase(repository, botApi, botToEvaluateName);
        return useCase.getResults(botNames);
    }


    private void printAvailableBots(List<String> botNames) {
        BotsAvailablePrinter printer = new BotsAvailablePrinter(botNames);
        printer.execute();
    }

    private int scanBotOption(List<String> botNames) {
        BotOptionReader scanOptions = new BotOptionReader(botNames);
        return scanOptions.execute();
    }

    private void printWaitingMessage() {
        WaitingMessagePrinter messagePrinter = new WaitingMessagePrinter();
        messagePrinter.execute();
    }

    private void printResultEvaluateBot(EvaluateResultsDto resultsDto, String botName) {
        EvaluateBotsPrinter printer = new EvaluateBotsPrinter(resultsDto, botName);
        printer.execute();
    }
}
