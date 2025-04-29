package com.bueno.domain.usecases.bot.providers.service;

import com.bueno.spi.service.BotServiceProvider;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.bueno.domain.usecases.bot.providers.service.RemoteBotsFileReader.read;

public class BotProviderService {

    public static final String PATH = "D:/Documentos/EngSoftware/IC/CTruco/domain/src/main/java/com/bueno/domain/usecases/bot/providers/RemoteBots.txt";

    public static BotServiceProvider load(String botServiceName) {
        final Predicate<BotServiceProvider> hasName = botImpl -> botImpl.getName().equals(botServiceName);
        final Optional<BotServiceProvider> possibleBot = providers().filter(hasName).findAny();
        return possibleBot.orElseThrow(() -> new NoSuchElementException("Service implementation not available: " + botServiceName));
    }

    public static List<String> providersNames() {
        return providers().map(BotServiceProvider::getName).filter(Objects::nonNull).collect(Collectors.toList());
    }

    private static Stream<BotServiceProvider> providers() {
        Stream<BotServiceProvider> spiProviders = ServiceLoader.load(BotServiceProvider.class).stream().map(ServiceLoader.Provider::get);
        List<BotServiceProvider> bots = new ArrayList<>(spiProviders.toList());
        List<BotServiceProvider> remoteOnes = read(PATH);
        bots.addAll(remoteOnes);
        return bots.stream();
    }

}
