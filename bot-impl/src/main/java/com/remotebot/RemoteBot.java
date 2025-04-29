package com.remotebot;

import com.bueno.spi.model.CardToPlay;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.service.BotServiceProvider;

import java.io.IOException;

public class RemoteBot implements BotServiceProvider {
    private static final String URL = "http://localhost:8080/bot/";

    private <R> R performerRequest(String url,GameIntel intel,Class<R> type){
        HttpRequester<GameIntel,R> requester = new PerformRequestService<>();

        try {
            return requester.post(url,intel,type);
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean getMaoDeOnzeResponse(GameIntel intel) {
        String url = URL + "mao-de-onze";
        return performerRequest(url,intel,Boolean.class);
    }

    @Override
    public boolean decideIfRaises(GameIntel intel) {
        String url = URL + "if-raises";
        return performerRequest(url,intel,Boolean.class);
    }

//    @Override
//    public CardToPlay chooseCard(GameIntel intel) {
//        String url = URL + "choose-card";
//        CardToPlayRecord cardToPlayRecord = performerRequest(url, intel, CardToPlayRecord.class);
//        CardRank rank = cardToPlayRecord.trucoCard().rank();
//        CardSuit suit = cardToPlayRecord.trucoCard().suit();
//        TrucoCard trucoCard = TrucoCard.of(rank,suit);
//        System.out.println(trucoCard);
//        return cardToPlayRecord.discard() ? CardToPlay.discard(trucoCard) : CardToPlay.of(trucoCard);
//    }

    @Override
    public CardToPlay chooseCard(GameIntel intel) {
        String url = URL + "choose-card";
        return performerRequest(url, intel, CardToPlay.class);
    }

    @Override
    public int getRaiseResponse(GameIntel intel) {
        String url = URL + "raise-response";
        return performerRequest(url,intel,Integer.class);
    }

    public boolean heathCheck(){
        String url = URL + "health-check";
        return false;
    }

//    @Override
//    public String getName() {
//        return BotServiceProvider.super.getName();
//    }
}
