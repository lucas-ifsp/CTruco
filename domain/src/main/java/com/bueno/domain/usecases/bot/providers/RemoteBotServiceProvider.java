package com.bueno.domain.usecases.bot.providers;

import com.bueno.domain.usecases.bot.dtos.RemoteBotDto;
import com.bueno.spi.model.CardToPlay;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.service.BotServiceProvider;

public class RemoteBotServiceProvider implements BotServiceProvider {

    private final RemoteBotApi api;
    private final RemoteBotDto botData;

    public RemoteBotServiceProvider(RemoteBotApi api, RemoteBotDto botData) {
        this.api = api;
        this.botData = botData;
    }

    @Override
    public boolean getMaoDeOnzeResponse(GameIntel intel) {
        return api.fetchMaoDeOnzeResponse(intel, botData);
    }

    @Override
    public boolean decideIfRaises(GameIntel intel) {
        return api.fetchRaiseRequestDecision(intel, botData);
    }

    @Override
    public CardToPlay chooseCard(GameIntel intel) {
        return api.fetchCardToPlay(intel, botData);
    }

    @Override
    public int getRaiseResponse(GameIntel intel) {
        return api.fetchRaiseResponse(intel, botData);
    }

    @Override
    public String getName() {
        return botData.name();
    }

}
