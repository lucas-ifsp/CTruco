package com.daniel.mateus.theroverbot;

import com.bueno.spi.model.CardToPlay;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;
import com.bueno.spi.service.BotServiceProvider;

public class TheRover implements BotServiceProvider {

    @Override
    public boolean getMaoDeOnzeResponse(GameIntel intel) {
        return false;
    }

    @Override
    public boolean decideIfRaises(GameIntel intel) {
        return false;
    }

    @Override
    public CardToPlay chooseCard(GameIntel intel) {

        return CardToPlay.of(intel.getCards().get(0));
    }

    @Override
    public int getRaiseResponse(GameIntel intel) {
        return 0;
    }

    @Override
    public String getName() {
        return "The Rover";
    }

    public boolean isPlayingFirst(GameIntel intel) {
        return intel.getOpponentCard().isEmpty();
    }

    public int getCurrentRound (GameIntel intel) {
        int cardsInHand = intel.getCards().size();
        if (cardsInHand == 3) return 1;
        if (cardsInHand == 2) return 2;
        if (cardsInHand == 1) return 3;
        return -1;
    }

    public TrucoCard chooseCardFirstHand (GameIntel intel) {
        return null;
    }

}
