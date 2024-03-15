package com.bueno.application.standalone;

import com.bueno.domain.usecases.bot.providers.BotProviders;
import com.bueno.domain.usecases.game.PlayWithBotsUseCase;

import java.util.UUID;

public class EvaluateBot {
    private final UUID uuidBotToEvaluate = UUID.randomUUID();

    public void botTester(){
        final var prompt = new UserPrompt();
        final var botNames = BotProviders.availableBots();

        prompt.printAvailableBots(botNames);

        final var botToEvaluatePosition = prompt.scanBotOption(botNames);

        String botToEvaluateName = botNames.get(botToEvaluatePosition - 1);

        final int times = 30;

        for(String challengedBotName:botNames){
            if (! challengedBotName.equals(botToEvaluateName)){
                UUID uuidChallengedBot = UUID.randomUUID();
                PlayWithBots.playBotsStarter(prompt,uuidBotToEvaluate,botToEvaluateName,uuidChallengedBot,challengedBotName,times);
            }
        }
    }
}
