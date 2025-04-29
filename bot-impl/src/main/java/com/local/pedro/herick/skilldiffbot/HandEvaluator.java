package com.local.pedro.herick.skilldiffbot;

import com.bueno.spi.model.TrucoCard;

import java.util.List;

class HandEvaluator {
    private static final int ZAP_VALUE = 13;
    private static final int COPAS_VALUE = 12;
    private static final int ESPADILHA_VALUE = 11;

    /**
     * Avalia força numérica da mão (0-1)
     */
    public double evaluateHand(List<TrucoCard> cards, TrucoCard vira) {
        int maxPossibleScore = switch (cards.size()) {
            case 3 -> ZAP_VALUE + COPAS_VALUE + ESPADILHA_VALUE;
            case 2 -> ZAP_VALUE + COPAS_VALUE;
            case 1 -> ZAP_VALUE;
            default -> 0;
        };

        double score = cards.stream().mapToDouble(card -> card.relativeValue(vira)).sum();
        return score / maxPossibleScore;
    }
}
