package com.gusmao.matos.clojurebot;


import com.bueno.spi.model.CardToPlay;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;

import java.util.List;
import java.util.Optional;

public final class HandUtils {
    private HandUtils() {}

    public static int getHandPower(GameIntel intel, TrucoCard vira) {
        return intel.getCards().stream().mapToInt(trucoCard -> trucoCard.relativeValue(vira)).sum();
    }

    public static CardToPlay getLessCardToWin(List<TrucoCard> cards, TrucoCard opponentCard, TrucoCard vira) {
        final Optional<TrucoCard> optionalCard = cards.stream().filter(card -> card.compareValueTo(opponentCard, vira) > 0).findFirst();

        if (optionalCard.isPresent()) {
            return CardToPlay.of(optionalCard.get());
        }

        return null;
    }
}
