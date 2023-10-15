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

    static Boolean hasCasalMaior(List<TrucoCard> cards, TrucoCard vira) {
        Boolean hasCasalMaior = cards.stream().anyMatch(card -> card.isZap(vira)) && cards.stream().anyMatch(card -> card.isCopas(vira));
        return hasCasalMaior;
    }

    static Boolean hasCasalMenor(List<TrucoCard> cards, TrucoCard vira) {
        Boolean hasCasalMenor = cards.stream().anyMatch(card -> card.isOuros(vira)) && cards.stream().anyMatch(card -> card.isEspadilha(vira));
        return hasCasalMenor;
    }
}
