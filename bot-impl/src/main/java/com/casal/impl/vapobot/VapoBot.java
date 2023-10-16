package com.casal.impl.vapobot;

import com.bueno.spi.model.CardToPlay;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;
import com.bueno.spi.service.BotServiceProvider;

import java.util.ArrayList;
import java.util.List;

public class VapoBot implements BotServiceProvider {
    private List<TrucoCard> opponentCardsThatHaveBeenPlayed = new ArrayList<>();

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

    public TrucoCard getHighestCard(GameIntel intel) {
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

    double getAverageCardValue(GameIntel intel) {
        int values = 0;
        for (TrucoCard card : intel.getCards()) {
            values += card.relativeValue(intel.getVira());
        }
        double average = (double) values / intel.getCards().size();
        return average;
    }

    boolean hasZap(GameIntel intel) {
        List<TrucoCard> myCards = intel.getCards();
        TrucoCard vira = intel.getVira();
        boolean zap = false;

        for (TrucoCard card : myCards) {
            if (card.isZap(vira)) {
                zap = true;
            }
        }

        return zap;
    }

    GameIntel.RoundResult getLastRoundResult(GameIntel intel) {
        if (intel.getRoundResults().isEmpty()) {
            throw new ArrayIndexOutOfBoundsException("There is no last round played.");
        }
        return intel.getRoundResults().get(intel.getRoundResults().size()-1);
    }

    int getRoundNumber (GameIntel intel) {
        return intel.getRoundResults().size() + 1;
    }


}
