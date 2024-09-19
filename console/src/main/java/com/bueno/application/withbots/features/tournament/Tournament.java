package com.bueno.application.withbots.features.tournament;

import com.bueno.domain.usecases.bot.providers.BotManagerService;
import com.bueno.domain.usecases.bot.providers.RemoteBotApi;
import com.bueno.domain.usecases.bot.repository.RemoteBotRepository;
import com.bueno.persistence.repositories.RemoteBotRepositoryImpl;
import com.remote.RemoteBotApiAdapter;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class Tournament {
    private final UUID tournamentUUID;
    private final int size;

    public Tournament(UUID tournamentUUID, int size) {
        this.tournamentUUID = tournamentUUID;
        this.size = size;
    }

    public static void createCamp(List<String> bot, int size) {
        Match[] arr = new Match[size - 1];
        RemoteBotRepository repository = new RemoteBotRepositoryImpl();
        RemoteBotApi api = new RemoteBotApiAdapter();
        BotManagerService botManagerService = new BotManagerService(repository, api);
        System.out.println(arr.length);
        int botIndex = 0;
        bot.forEach(System.out::println);

        insertMatches(bot, size, arr, botIndex);
        setNextMatches(size, arr);
        Arrays.stream(arr).forEach(System.out::println);
        System.out.println();
        refreshMatches(arr);
        Arrays.stream(arr).forEach(System.out::println);
        setWinners(arr, size, api, repository, botManagerService);
        System.out.println();
        refreshMatches(arr);
        Arrays.stream(arr).forEach(System.out::println);
    }

    private static void setNextMatches(int size, Match[] arr) {
        int next = size / 2 - 1;
        for (int i = 0; i < size - 1; i++) {
            if (i % 2 == 0) next++;
            if (i == size - 2) break;
            arr[i].setNext(arr[next]);
        }
    }

    private static void insertMatches(List<String> bot, int size, Match[] arr, int botIndex) {
        for (int i = 0; i < size - 1; i++) {
            if (i < size / 2) {
                arr[i] = new Match(UUID.randomUUID(),
                        bot.get(botIndex),
                        bot.get(botIndex + 1),
                        false,
                        null,
                        0,
                        0,
                        null);
                botIndex++;
                botIndex++;
            } else {
                arr[i] = new Match(UUID.randomUUID(),
                        null,
                        null,
                        false,
                        null,
                        0,
                        0,
                        null);
            }
        }
    }

    public static void setWinners(Match[] arr, int size, RemoteBotApi api, RemoteBotRepository repository, BotManagerService botManagerService) {

        for (Match m : arr) {
            if (m.isAvailable()) {
                m.play(repository, api, botManagerService, 11);
            }
        }

    }

    public static void refreshMatches(Match[] arr) {
        for (Match match : arr) {
            if (match.getNext() != null && match.getWinnerName() != null) {
                if (match.getNext().getP1Name() == null) {
                    match.getNext().setP1Name(match.getWinnerName());
                    continue;
                }
                if (match.getNext().getP2Name() == null) {
                    match.getNext().setP2Name(match.getWinnerName());
                }
            }
            if (match.getWinnerName() == null && match.getP1Name() != null && match.getP2Name() != null) {
                match.setAvailable(true);
            }
        }
    }

    public static void playAvailableMatches(Match[] arr) {
        for (Match match : arr) {
            if (match.isAvailable()) {
                match.setAvailable(false);
            }
        }
    }
}
