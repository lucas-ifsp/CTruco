package com.remote;

import com.bueno.spi.model.CardToPlay;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.service.BotServiceProvider;

public record RemoteBot(String url) implements BotServiceProvider {

    private <R> R performerPostRequest(String url, GameIntel intel, Class<R> type) {
        HttpRequester<GameIntel, R> requester = new PerformRequestService<>();
        return requester.post(url, intel, type);
    }

    private <R> R performerGetRequest(String url, Class<R> type) {
        HttpRequester<GameIntel, R> requester = new PerformRequestService<>();
        return requester.get(url, type);
    }

    @Override
    public boolean getMaoDeOnzeResponse(GameIntel intel) {
        String url = this.url + "mao-de-onze";
        return performerPostRequest(url, intel, Boolean.class);
    }

    @Override
    public boolean decideIfRaises(GameIntel intel) {
        String url = this.url + "if-raises";
        return performerPostRequest(url, intel, Boolean.class);
    }

    @Override
    public CardToPlay chooseCard(GameIntel intel) {
        String url = this.url + "choose-card";
        return performerPostRequest(url, intel, CardToPlay.class);
    }

    @Override
    public int getRaiseResponse(GameIntel intel) {
        String url = this.url + "raise-response";
        return performerPostRequest(url, intel, Integer.class);
    }

    @Override
    public String getName() {
        String url = this.url + "name";
        return performerGetRequest(url, String.class);
    }
}
