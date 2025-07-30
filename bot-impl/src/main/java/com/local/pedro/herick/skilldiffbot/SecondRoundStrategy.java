package com.local.pedro.herick.skilldiffbot;


//Adaptação baseada no resultado da primeira rodada
//Threshold moderado
//Considerar se estamos ganhando/perdendo
//Preservar cartas fortes se ganhou primeira

import com.bueno.spi.model.CardToPlay;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;

import java.util.List;

public class SecondRoundStrategy extends BotUtils implements Strategy {
    private final HandEvaluator evaluator = new HandEvaluator();
    private static final double LOSING_THRESHOLD = 0.4;
    private static final double WINNING_THRESHOLD = 0.6;

    @Override
    public boolean decideIfRaises(GameIntel intel) {
        if (wonFirstRound(intel)) {
            return evaluator.evaluateHand(intel.getCards(), intel.getVira()) > WINNING_THRESHOLD;
        }

        return evaluator.evaluateHand(intel.getCards(), intel.getVira()) > LOSING_THRESHOLD;
    }

    @Override
    public CardToPlay chooseCard(GameIntel intel) {
        List<TrucoCard> cards = intel.getCards();
        TrucoCard vira = intel.getVira();

        if (wonFirstRound(intel)) {
            return CardToPlay.of(getWeakestCard(cards, vira));
        }

        if (intel.getOpponentCard().isPresent()) return CardToPlay.of(getWeakestCardToWin(intel));

        return CardToPlay.of(getStrongestCard(cards, vira));
    }
}