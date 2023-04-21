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
        return true;
    }

    @Override
    public boolean decideIfRaises(GameIntel intel) {
        TrucoCard vira = intel.getVira();

        List<TrucoCard> openCards = new ArrayList<>(intel.getCards().subList(1, 3));
        openCards.sort((c1, c2) -> {
            return c1.compareValueTo(c2, vira);
        });

        GameIntel.RoundResult roundResult = intel.getRoundResults().get(0);

        switch (roundResult) {
            case WON -> {
                if (openCards.get(1).getRank().value() >= 8) {
                    return calculateCurrentHandValue(intel) <= 8;
                }
            }
        }
        return false;
    }

    @Override
    public CardToPlay chooseCard(GameIntel intel) {
        TrucoCard vira = intel.getVira();

        List<TrucoCard> cards = new ArrayList<>(intel.getCards());
        cards.sort((c1, c2) -> {
            return c1.compareValueTo(c2, vira);
        });

        GameIntel.RoundResult roundResult = intel.getRoundResults().get(0);

        switch (roundResult) {
            case WON -> {
                if (hasZap(intel)) return CardToPlay.discard(cards.get(0));
                else if (calculateCurrentHandValue(intel) >= 17) return CardToPlay.of(cards.get(0));
            }
        }

        return null;
    }

    private boolean hasZap(GameIntel intel) {
        return intel.getCards().stream().anyMatch(card -> card.isZap(intel.getVira()));
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
