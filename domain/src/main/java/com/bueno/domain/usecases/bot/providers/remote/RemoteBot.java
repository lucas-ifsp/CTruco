package com.bueno.domain.usecases.bot.providers.remote;

import com.bueno.spi.model.CardToPlay;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.service.BotServiceProvider;

public record RemoteBot(String url) implements BotServiceProvider {
    @Override
    public boolean getMaoDeOnzeResponse(GameIntel intel) {
        String url = this.url + "mao-de-onze";
        HttpRequester<GameIntel, Boolean> requester = new PerformRequestService<>();
        return requester.post(url, intel, Boolean.class);
    }

    @Override
    public boolean decideIfRaises(GameIntel intel) {
        String url = this.url + "if-raises";
        HttpRequester<GameIntel, Boolean> requester = new PerformRequestService<>();
        return requester.post(url, intel, Boolean.class);
    }

    @Override
    public CardToPlay chooseCard(GameIntel intel) {
        String url = this.url + "choose-card";
        HttpRequester<GameIntel, CardToPlay> requester = new PerformRequestService<>();
        return requester.post(url, intel, CardToPlay.class);
    }

    @Override
    public int getRaiseResponse(GameIntel intel) {
        String url = this.url + "raise-response";
        HttpRequester<GameIntel, Integer> requester = new PerformRequestService<>();
        return requester.post(url, intel, Integer.class);
    }

    @Override
    public String getName() {
        String url = this.url + "name";
        HttpRequester<GameIntel, String> requester = new PerformRequestService<>();
        return requester.get(url, String.class);
    }
}
