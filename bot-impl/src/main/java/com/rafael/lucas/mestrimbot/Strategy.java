package com.rafael.lucas.mestrimbot;

import com.rafael.lucas.mestrimbot.StrategyRoundOne;
import com.rafael.lucas.mestrimbot.StrategyRoundThree;
import com.rafael.lucas.mestrimbot.StrategyRoundTwo;
import com.bueno.spi.model.CardRank;
import com.bueno.spi.model.CardToPlay;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;

import java.util.List;

abstract sealed class Strategy permits StrategyRoundOne, StrategyRoundTwo, StrategyRoundThree {
    static Strategy of(GameIntel intel) {
        int currentRound = intel.getRoundResults().size();
        if (currentRound == 0) return new StrategyRoundOne(intel);
        if (currentRound == 1) return new StrategyRoundTwo(intel);
        return new StrategyRoundThree(intel);
    }

    abstract boolean decideIfRaises();
    abstract int getRaiseResponse();
    abstract CardToPlay chooseCard();

    protected int getCardValue(TrucoCard card, TrucoCard vira) {
        return card.relativeValue(vira) + (card.getRank().value() > vira.getRank().value() ? 1 : 0);
    }

    protected List<TrucoCard> sortCards(List<TrucoCard> cards, TrucoCard vira) {
        return cards.stream().sorted((c1, c2) -> c1.compareValueTo(c2, vira)).toList();
    }

    protected boolean haveManilhas(List<TrucoCard> cards, TrucoCard vira) {
        return cards.stream().anyMatch(card -> card.isManilha(vira));
    }

    protected int getHandStrength(List<TrucoCard> cards, TrucoCard vira) {
        return cards.stream().mapToInt(card -> getCardValue(card, vira)).sum();
    }

    protected boolean isStrongCard(TrucoCard card, TrucoCard vira) {
        return card.relativeValue(vira) >= CardRank.KING.value();
    }

    public boolean getMaoDeOnzeResponse(GameIntel intel) {
        List<TrucoCard> cards = sortCards(intel.getCards(), intel.getVira());
        return getHandStrength(cards, intel.getVira()) >= 24;
    }
}
