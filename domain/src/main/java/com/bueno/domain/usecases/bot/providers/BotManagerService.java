package com.bueno.domain.usecases.bot.providers;

import com.bueno.domain.usecases.bot.dtos.RemoteBotDto;
import com.bueno.domain.usecases.bot.repository.RemoteBotRepository;
import com.bueno.spi.service.BotServiceProvider;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class BotManagerService {

    private static final int HEALTH_CHECK_PERIOD = 2;

    private final RemoteBotRepository repository;
    private final RemoteBotApi api;
    private final List<BotServiceProvider> remoteBotsCache = new ArrayList<>();
    private Instant lastCheck;

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

        if (lastCheck == null || Duration.between(lastCheck, Instant.now()).toMinutes() > HEALTH_CHECK_PERIOD) {
            remoteBotsCache.addAll(repository.findAll().stream()
                    .filter(this::isHealth)
                    .map(this::toBotServiceProvider)
                    .toList());
            lastCheck = Instant.now();
        }
        bots.addAll(remoteBotsCache);

        return bots.stream();
    }

    private boolean isHealth(RemoteBotDto dto) {
        return api.isHealthy(dto);
    }

    private BotServiceProvider toBotServiceProvider(RemoteBotDto dto) {
        return new RemoteBotServiceProvider(api, dto);
    }
}
