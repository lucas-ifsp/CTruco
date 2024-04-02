package com.bueno.application.withbots.features;

import com.bueno.application.withbots.commands.BotRankPrinter;
import com.bueno.application.withbots.commands.WaitingMessagePrinter;
import com.bueno.domain.usecases.game.usecase.RankBotsUseCase;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class RankBots {

    public void allBots() {
        RankBotsUseCase useCase = new RankBotsUseCase();

        showWaitingMessage();

        Map<String, Long> rankMap = useCase.rankAll();
        rankMap = sortByValueDescending(rankMap);

        printRank(rankMap);
    }

    private void printRank(Map<String, Long> rankMap) {
        BotRankPrinter printer = new BotRankPrinter(rankMap);
        printer.execute();
    }

    private void showWaitingMessage() {
        WaitingMessagePrinter messagePrinter = new WaitingMessagePrinter();
        messagePrinter.execute();
    }

    private Map<String, Long> sortByValueDescending(Map<String, Long> mapToSort) {
        return  mapToSort.entrySet().stream()
                .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
    }

}
