package com.gusmao.matos.clojurebot;


import com.bueno.spi.model.CardToPlay;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;

import java.util.List;

public final class HandUtils {
    private HandUtils() {
    }

    public static int getHandPower(GameIntel intel, TrucoCard vira) {
        return intel.getCards().stream().mapToInt(trucoCard -> trucoCard.relativeValue(vira)).sum();
    }

    public static CardToPlay getLessCardToWin(List<TrucoCard> cards, TrucoCard opponentCard, TrucoCard vira) {
        for (TrucoCard card : cards) {
            if (card.compareValueTo(opponentCard, vira) > 0) {
                return CardToPlay.of(card);
            }
        }
        return CardToPlay.of(cards.get(cards.size() - 1));
    }
}
