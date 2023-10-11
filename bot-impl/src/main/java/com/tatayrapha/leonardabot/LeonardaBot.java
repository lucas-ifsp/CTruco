package com.tatayrapha.leonardabot;

import com.bueno.spi.model.CardToPlay;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;
import com.bueno.spi.service.BotServiceProvider;

import java.util.Comparator;
import java.util.List;

public class LeonardaBot implements BotServiceProvider {
    @Override
    public boolean getMaoDeOnzeResponse(GameIntel intel) {
        return false;
    }

    @Override
    public boolean decideIfRaises(GameIntel intel) {
        if (intel.getScore() == 10) {
            return true;
        }
        return (intel.getCards().stream().anyMatch(card -> card.isZap(intel.getVira())));
    }


    @Override
    public CardToPlay chooseCard(GameIntel intel) {
        List<TrucoCard> cards = intel.getCards();
        TrucoCard chosenCard = cards.stream().max(Comparator.comparing(TrucoCard::getRank)).orElse(cards.get(0));
        return CardToPlay.of(chosenCard);
    }


    @Override
    public int getRaiseResponse(GameIntel intel) {
        return 0;
    }

    @Override
    public String getName() {
        return BotServiceProvider.super.getName();
    }
}
