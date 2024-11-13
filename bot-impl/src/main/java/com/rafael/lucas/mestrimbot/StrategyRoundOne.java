package com.rafael.lucas.mestrimbot;

import com.bueno.spi.model.CardToPlay;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;

import java.util.List;

final class StrategyRoundOne extends Strategy {
    private final GameIntel intel;
    private final List<TrucoCard> cards;
    private final TrucoCard vira;

    StrategyRoundOne(GameIntel intel) {
        this.intel = intel;
        this.vira = intel.getVira();
        this.cards = sortCards(intel.getCards(), vira);
    }

    @Override
    CardToPlay chooseCard() {
        return CardToPlay.of(cards.get(1));
    }

    @Override
    int getRaiseResponse() {
        return getHandStrength(cards, vira) >= 20 ? 1 : -1;
    }

    @Override
    boolean decideIfRaises() {
        return getHandStrength(cards, vira) >= 22 || haveManilhas(cards, vira);
    }
}
