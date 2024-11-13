package com.rafael.lucas.mestrimbot;

import com.bueno.spi.model.CardToPlay;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;

import java.util.List;

final class StrategyRoundTwo extends Strategy {
    private final GameIntel intel;
    private final List<TrucoCard> cards;
    private final TrucoCard vira;

    StrategyRoundTwo(GameIntel intel) {
        this.intel = intel;
        this.vira = intel.getVira();
        this.cards = sortCards(intel.getCards(), vira);
    }

    @Override
    CardToPlay chooseCard() {
        if (cards.size() > 1 && intel.getRoundResults().size() > 0) {
            if (intel.getRoundResults().get(0) == GameIntel.RoundResult.WON) {
                return CardToPlay.of(cards.get(cards.size() - 1));
            } else {
                return CardToPlay.of(cards.get(1));
            }
        } else {
            return CardToPlay.of(cards.get(0)); // Fallback para o caso de apenas 1 carta
        }
    }

    @Override
    int getRaiseResponse() {
        return getHandStrength(cards, vira) >= 22 ? 0 : -1;
    }

    @Override
    boolean decideIfRaises() {
        return getHandStrength(cards, vira) >= 23 || (haveManilhas(cards, vira) && isStrongCard(cards.get(0), vira));
    }
}
