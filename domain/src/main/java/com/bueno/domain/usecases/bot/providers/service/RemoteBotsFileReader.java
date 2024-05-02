package com.bueno.domain.usecases.bot.providers.service;

import com.bueno.domain.usecases.bot.providers.remote.RemoteBot;
import com.bueno.spi.service.BotServiceProvider;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.function.Function;

public class RemoteBotsFileReader {

    public static List<BotServiceProvider> read(String pathName) {
        return mapLinesToRemoteBotInfo(scanLines(pathName));
    }

    private static List<String> scanLines(String pathName) {
        List<String> lines = new ArrayList<>();
        File file = new File(pathName);
        Scanner scan;
        try {
            scan = new Scanner(file);
            while (scan.hasNextLine()) {
                String content = scan.nextLine();
                lines.add(content);
            }
        } catch (FileNotFoundException e) {
            System.err.println("Could not find file: " + pathName + ". Remote bots will not be available.");
        }
        return lines;
    }

    private static List<BotServiceProvider> mapLinesToRemoteBotInfo(List<String> lines) {
        Function<String,BotServiceProvider> mapToBotServiceProvider = RemoteBot::new;
        return lines.stream().map(mapToBotServiceProvider).toList();
    }


}
