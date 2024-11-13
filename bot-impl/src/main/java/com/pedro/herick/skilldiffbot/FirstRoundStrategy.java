package com.pedro.herick.skilldiffbot;

import com.bueno.spi.model.CardToPlay;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;

import java.util.List;

class FirstRoundStrategy extends BotUtils implements Strategy {
    private final HandEvaluator evaluator = new HandEvaluator();
    private static final double RAISE_BASE_THRESHOLD = 0.5;
    private static final double RAISE_THRESHOLD_WITH_MANILHA = 0.6;

    @Override
    public boolean decideIfRaises(GameIntel intel) {
        List<TrucoCard> cards = intel.getCards();
        TrucoCard vira = intel.getVira();

        if (hasManilha(cards, vira)) {
            List<TrucoCard> cardsWithoutZap = cards.stream().filter(c -> c.isZap(vira)).toList();
            return evaluator.evaluateHand(cardsWithoutZap, vira) > RAISE_THRESHOLD_WITH_MANILHA;
        }

        return evaluator.evaluateHand(intel.getCards(), intel.getVira()) > RAISE_BASE_THRESHOLD;
    }

    @Override
    public CardToPlay chooseCard(GameIntel intel) {
        List<TrucoCard> cards = intel.getCards();
        TrucoCard vira = intel.getVira();

        if (hasManilha(cards, vira) && evaluator.evaluateHand(cards, vira) > RAISE_BASE_THRESHOLD) {
            return CardToPlay.of(getWorstCard(cards, vira));
        }

        if (intel.getOpponentCard().isPresent()) return CardToPlay.of(getBestCardComparedToOpponent(intel));

        return CardToPlay.of(getBestCard(cards, vira));
    }
}
