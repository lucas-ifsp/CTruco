package com.bonelli.noli.paulistabot;

import com.bueno.spi.model.CardToPlay;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class SecondRound implements Strategy {

    public SecondRound() {
    }

    @Override
    public int getRaiseResponse(GameIntel intel) {
        return 0;
    }

    @Override
    public boolean getMaoDeOnzeResponse(GameIntel intel) {
        List<TrucoCard> cards = new ArrayList<>(intel.getCards());
        cards.sort((c1, c2) -> {
            return c1.compareValueTo(c2, intel.getVira());
        });

        TrucoCard cardMedium = cards.get(0);

        if (calculateCurrentHandValue(intel) >= 25) {
            if (hasManilha(intel)) return true;
            else if (intel.getOpponentScore() < 9 && hasTwoOrThree(intel)) return true;
            else return intel.getOpponentScore() >= 9 && cardMedium.getRank().value() >= 9;
        } else return false;
    }

    @Override
    public boolean decideIfRaises(GameIntel intel) {
        return false;
    }

    @Override
    public CardToPlay chooseCard(GameIntel intel) {
        TrucoCard vira = intel.getVira();
        GameIntel.RoundResult roundResult = intel.getRoundResults().get(0);

        return null;
    }

    @Override
    public boolean hasManilha(GameIntel intel) {
        return false;
    }

    private boolean hasTwoOrThree(GameIntel intel) {
        return intel.getCards().stream().anyMatch(card -> card.isManilha(intel.getVira()));
    }

    private int calculateCurrentHandValue(GameIntel intel) {
        if (intel.getCards().isEmpty()) return 0;
        return intel.getCards().stream().map(card -> card.relativeValue(intel.getVira())).reduce(Integer::sum).orElseThrow();
    }
}
