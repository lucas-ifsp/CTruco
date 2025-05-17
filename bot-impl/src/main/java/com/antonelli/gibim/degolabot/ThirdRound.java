package com.antonelli.gibim.degolabot;

import com.bueno.spi.model.CardToPlay;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;

import java.util.Comparator;
import java.util.List;

public class ThirdRound implements Strategy {

    @Override
    public int getRaiseResponse(GameIntel intel) {
        return -1;
    }

    @Override
    public boolean decideIfRaises(GameIntel intel) {
        return false;
    }

    @Override
    public CardToPlay chooseCard(GameIntel intel) {
        List<TrucoCard> cards = intel.getCards();
        return CardToPlay.of(selectAnyCard(cards, intel));
    }

    private TrucoCard selectAnyCard(List<TrucoCard> cards, GameIntel intel) {
        return cards.stream()
                .min(Comparator.comparingInt(c -> c.relativeValue(intel.getVira())))
                .orElse(cards.get(0));
    }
}
