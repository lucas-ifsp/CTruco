package com.bonelli.noli.paulistabot;

import com.bueno.spi.model.CardRank;
import com.bueno.spi.model.CardToPlay;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ThirdRound implements Strategy {

    public ThirdRound() {
    }

    @Override
    public int getRaiseResponse(GameIntel intel) {
        return -1;
    }

    @Override
    public boolean getMaoDeOnzeResponse(GameIntel intel) {
        TrucoCard cardMedium = getCardWithMediumStrength(intel.getCards(), intel.getVira());

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
        return CardToPlay.of(intel.getCards().get(0));
    }

    @Override
    public boolean hasManilha(GameIntel intel) {
        return false;
    }

    public boolean hasTwoOrThree(GameIntel intel) {
        return intel.getCards().stream().anyMatch(card -> card.getRank() == CardRank.TWO
                || card.getRank() == CardRank.THREE);
    }

    public TrucoCard getCardWithMediumStrength(List<TrucoCard> cards, TrucoCard vira) {
        return cards.stream()
                .sorted(Comparator.comparingInt(carta -> carta.relativeValue(vira)))
                .skip(1)
                .limit(1)
                .findFirst()
                .orElseThrow();
    }

    public int calculateCurrentHandValue(GameIntel intel) {
        if (intel.getCards().isEmpty()) return 0;
        return intel.getCards().stream().map(card -> card.relativeValue(intel.getVira())).reduce(Integer::sum).orElseThrow();
    }
}
