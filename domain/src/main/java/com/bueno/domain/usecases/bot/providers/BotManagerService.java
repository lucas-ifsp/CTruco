package com.bueno.domain.usecases.bot.providers;

import com.bueno.domain.usecases.bot.repository.RemoteBotDto;
import com.bueno.domain.usecases.bot.repository.RemoteBotRepository;
import com.bueno.spi.service.BotServiceProvider;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class BotManagerService {

    private final RemoteBotRepository repository;
    private final RemoteBotApi api;

    public BotManagerService(RemoteBotRepository repository, RemoteBotApi api) {
        this.repository = repository;
        this.api = api;
    }

    public BotServiceProvider load(String botServiceName) {
        final Predicate<BotServiceProvider> hasName = botImpl -> botImpl.getName().equals(botServiceName);
        final Optional<BotServiceProvider> possibleBot = providers().filter(hasName).findAny();
        return possibleBot.orElseThrow(() -> new NoSuchElementException("Service implementation not available: " + botServiceName));
    }

    public List<String> providersNames() {
        return providers().map(BotServiceProvider::getName).filter(Objects::nonNull).collect(Collectors.toList());
    }

    private Stream<BotServiceProvider> providers() {
        Stream<BotServiceProvider> spiProviders = ServiceLoader.load(BotServiceProvider.class).stream()
                .map(ServiceLoader.Provider::get);

        List<BotServiceProvider> bots = new ArrayList<>(spiProviders.toList());

        List<BotServiceProvider> remoteOnes = repository.findAll().stream()
                .map(this::toBotServiceProvider)
                .toList();

        bots.addAll(remoteOnes);
        return bots.stream();
    }

    private BotServiceProvider toBotServiceProvider(RemoteBotDto dto) {
        return new RemoteBotServiceProvider(api, dto);
    }
}
