package com.eduardo.vinicius.camaleaotruqueiro;

import com.bueno.spi.model.TrucoCard;
import com.bueno.spi.model.GameIntel;

import java.util.List;

import static com.eduardo.vinicius.camaleaotruqueiro.TrucoUtils.*;

public enum HandsCardSituation {
    ALMOST_ABSOLUTE_VICTORY("Almost Absolute Victory", 4),
    ALMOST_CERTAIN_VICTORY("Almost Certain Victory", 3),
    BLUFF_TO_GET_POINTS("Bluff To Get Points", 2),
    BLUFF_TO_INTIMIDATE("Bluff To Intimidate", 1),
    ALMOST_CERTAIN_DEFEAT("Almost Certain Defeat", 0);

    private final String description;
    private final int ordinalValue;

    HandsCardSituation(String description, int ordinalValue) {
        this.description = description;
        this.ordinalValue = ordinalValue;
    }

    public static HandsCardSituation evaluateHandSituation(GameIntel intel) {
        List<TrucoCard> cards = intel.getCards();
        TrucoCard vira = intel.getVira();

        int manilhas = numberOfManilhas(cards, vira);
        int highRankCards = getNumberOfHighRankCards(cards, vira);
        int mediumRankCards = getNumberOfMediumRankCards(cards, vira);
        int lowRankCards = getNumberOfLowRankCards(cards, vira);

        // Vitória quase absoluta
        if (    (manilhas >= 2 && cards.size() >= 2) ||
                (manilhas == 1 && cards.size() == 1)
        ) {
            return ALMOST_ABSOLUTE_VICTORY;
        }

        // Vitória quase certa
        if (mediumRankCards == 0 && lowRankCards == 0) {
            return ALMOST_CERTAIN_VICTORY;
        }

        // Blefe para arrancar pontos
        if (    (highRankCards == 2 && mediumRankCards == 1) ||
                (highRankCards == 2 && lowRankCards == 1) ||
                (cards.size() == 2 && highRankCards == 1 && mediumRankCards == 1) ||
                (cards.size() == 2 && highRankCards == 1 && lowRankCards == 1)) {
            return BLUFF_TO_GET_POINTS;
        }

        // Blefe para intimidar
        if (    (highRankCards == 1 && mediumRankCards == 2) ||
                (highRankCards == 1 && mediumRankCards == 1 && lowRankCards == 1) ||
                (highRankCards == 1 && lowRankCards == 2) ) {
            return BLUFF_TO_INTIMIDATE;
        }
        // Caso nenhuma condição seja satisfeita, assumir derrota quase certa por padrão
        return ALMOST_CERTAIN_DEFEAT;
    }

    public String getDescription() {
        return description;
    }

    public int getOrdinalValue() {
        return ordinalValue;
    }

    @Override
    public String toString() {
        return description;
    }
}
