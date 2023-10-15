package com.casal.impl.vapobot;

import com.bueno.spi.model.CardToPlay;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;
import com.bueno.spi.service.BotServiceProvider;

public class VapoBot implements BotServiceProvider {

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
        return null;
    }

    @Override
    public int getRaiseResponse(GameIntel intel) {
        return 0;
    }

    @Override
    public String getName() {
        return BotServiceProvider.super.getName();
    }

    public TrucoCard getHighestCard(GameIntel intel){
        TrucoCard highestCard = intel.getCards().get(0);

        for (TrucoCard card : intel.getCards()) {
            if (card.relativeValue(intel.getVira()) > highestCard.relativeValue(intel.getVira())) {
                highestCard = card;
            }
        }

        return highestCard;
    }

    public TrucoCard getLowestCard(GameIntel intel) {
        TrucoCard lowestCard = intel.getCards().get(0);

        for (TrucoCard card : intel.getCards()) {
            if (lowestCard.relativeValue(intel.getVira()) > card.relativeValue(intel.getVira())) {
                lowestCard = card;
            }
        }

        return lowestCard;
    }
}
