package com.tatayrapha.leonardabot;

import com.bueno.spi.model.CardToPlay;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;
import com.bueno.spi.service.BotServiceProvider;

import java.util.Comparator;
import java.util.List;

public class LeonardaBot implements BotServiceProvider {
    private static final int QUIT_THRESHOLD = 9;
    private static final int ACCEPT_THRESHOLD = 10;

    @Override
    public boolean getMaoDeOnzeResponse(GameIntel intel) {
        return false;
    }

    @Override
    public boolean decideIfRaises(GameIntel intel) {
        int playerScore = intel.getScore();
        return playerScore >= QUIT_THRESHOLD || intel.getCards().stream().anyMatch(card -> card.isZap(intel.getVira()));
    }

    @Override
    public CardToPlay chooseCard(GameIntel intel) {
        List<TrucoCard> cards = intel.getCards();
        TrucoCard chosenCard = cards.stream().min(Comparator.comparing(TrucoCard::getRank)).orElse(cards.get(0));
        return CardToPlay.of(chosenCard);
    }

    @Override
    public int getRaiseResponse(GameIntel intel) {
        int playerScore = intel.getScore();
        if (playerScore < QUIT_THRESHOLD) {
            return -1;
        } else if (playerScore <= ACCEPT_THRESHOLD) {
            return intel.getCards().stream().anyMatch(card -> card.isZap(intel.getVira())) ? 1 : 0;
        } else {
            return intel.getCards().stream().anyMatch(card -> card.isZap(intel.getVira())) ? 1 : 0;
        }
    }

    @Override
    public String getName() {
        return BotServiceProvider.super.getName();
    }
}