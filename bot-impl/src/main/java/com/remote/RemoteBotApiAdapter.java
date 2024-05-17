package com.remote;

import com.bueno.domain.usecases.bot.providers.RemoteBotApi;
import com.bueno.domain.usecases.bot.repository.RemoteBotDto;
import com.bueno.spi.model.CardToPlay;
import com.bueno.spi.model.GameIntel;
import org.springframework.http.HttpMethod;

public class RemoteBotApiAdapter implements RemoteBotApi {

    @Override
    public boolean fetchMaoDeOnzeResponse(GameIntel intel, RemoteBotDto botData) {
        String url = buildUrl(botData, "mao-de-onze");
        HttpRequestService<GameIntel, Boolean> requester = new HttpRequestService<>();
        return requester.sendRequest(url, intel, HttpMethod.POST, Boolean.class);
    }

    @Override
    public boolean fetchRaiseRequestDecision(GameIntel intel, RemoteBotDto botData) {
        String url = buildUrl(botData, "if-raises");
        HttpRequestService<GameIntel, Boolean> requester = new HttpRequestService<>();
        return requester.sendRequest(url, intel, HttpMethod.POST, Boolean.class);
    }

    @Override
    public CardToPlay fetchCardToPlay(GameIntel intel, RemoteBotDto botData) {
        String url = buildUrl(botData, "choose-card");
        HttpRequestService<GameIntel, CardToPlay> requester = new HttpRequestService<>();
        return requester.sendRequest(url, intel, HttpMethod.POST, CardToPlay.class);
    }

    @Override
    public int fetchRaiseResponse(GameIntel intel, RemoteBotDto botData) {
        String url = buildUrl(botData, "raise-response");
        HttpRequestService<GameIntel, Integer> requester = new HttpRequestService<>();
        return requester.sendRequest(url, intel, HttpMethod.POST, Integer.class);
    }

    @Override
    public boolean isHealthy() {
        return false;
    }

    private String buildUrl(RemoteBotDto botData, String resource){
        return String.format("%s:%s/%s", botData.url(), botData.port(), resource);
    }
}
