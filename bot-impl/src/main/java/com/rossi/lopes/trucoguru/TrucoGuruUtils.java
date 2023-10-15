package com.rossi.lopes.trucoguru;

import com.bueno.spi.model.CardRank;
import com.bueno.spi.model.TrucoCard;

import java.util.List;

public class TrucoGuruUtils {
    static Boolean hasManilha(List<TrucoCard> cards, TrucoCard vira) {
        return cards.stream().anyMatch(card -> card.isManilha(vira));
    }

    static Boolean hasZap(List<TrucoCard> cards, TrucoCard vira) {
        return cards.stream().anyMatch(card -> card.isZap(vira));
    }

    static Boolean hasStrongCard(List<TrucoCard> cards, TrucoCard vira) {
        Boolean hasManilha = hasManilha(cards, vira);
        Boolean hasStrongCard = cards.stream().anyMatch(card -> card.getRank() == CardRank.THREE);
        return hasManilha || hasStrongCard;
    }
}
