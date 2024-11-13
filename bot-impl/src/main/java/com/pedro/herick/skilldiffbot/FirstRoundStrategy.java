package com.pedro.herick.skilldiffbot;

import com.bueno.spi.model.CardToPlay;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;

import java.util.List;
import java.util.Optional;

class FirstRoundStrategy extends BotUtils implements Strategy {
    private final HandEvaluator evaluator = new HandEvaluator();
    private static final double RAISE_THRESHOLD = 0.5;


    @Override
    public boolean decideIfRaises(GameIntel intel) {
        List<TrucoCard> cards = intel.getCards();
        TrucoCard vira = intel.getVira();

        if (hasZap(cards, vira)) {
            List<TrucoCard> cardsWithoutZap = cards.stream().filter(c -> c.isZap(vira)).toList();
            return evaluator.evaluateHand(cardsWithoutZap, vira) > RAISE_THRESHOLD;
        }

        return evaluator.evaluateHand(intel.getCards(), intel.getVira()) > RAISE_THRESHOLD;
    }

    @Override
    public CardToPlay chooseCard(GameIntel intel) {
        List<TrucoCard> cards = intel.getCards();
        TrucoCard vira = intel.getVira();

        Optional<TrucoCard> zap = cards.stream().filter(card -> card.isZap(vira)).findFirst();

        if (zap.isPresent()) return CardToPlay.of(getWorstCard(cards, vira));

        return CardToPlay.of(getBestCard(cards, vira));
    }
}
