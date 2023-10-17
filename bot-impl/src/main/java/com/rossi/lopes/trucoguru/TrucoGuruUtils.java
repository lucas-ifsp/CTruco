/*
 *  Copyright (C) 2022 Lucas B. R. de Oliveira - IFSP/SCL
 *  Contact: lucas <dot> oliveira <at> ifsp <dot> edu <dot> br
 *
 *  This file is part of CTruco (Truco game for didactic purpose).
 *
 *  CTruco is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  CTruco is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with CTruco.  If not, see <https://www.gnu.org/licenses/>
 */

// Authors: Juan Rossi e Guilherme Lopes

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

    static Boolean hasCopas(List<TrucoCard> cards, TrucoCard vira) {
        return cards.stream().anyMatch(card -> card.isCopas(vira));
    }

    static Boolean hasStrongCard(List<TrucoCard> cards, TrucoCard vira) {
        Boolean hasManilha = hasManilha(cards, vira);
        Boolean hasStrongCard = cards.stream().anyMatch(card -> card.getRank() == CardRank.THREE);
        return hasManilha || hasStrongCard;
    }

    static Boolean hasHighRank(List<TrucoCard> cards, TrucoCard vira) {
        return cards.stream()
                .filter(card -> !card.isManilha(vira))
                .anyMatch(card -> card.getRank().value() >= 8);
    }

    static Boolean hasStrongHand(List<TrucoCard> cards, TrucoCard vira) {
        boolean hasManilha = hasManilha(cards, vira);
        boolean hasHighRank = hasHighRank(cards, vira);

        return hasManilha && hasHighRank;
    }

    static Boolean hasCasalMaior(List<TrucoCard> cards, TrucoCard vira) {
        Boolean hasCasalMaior = cards.stream().anyMatch(card -> card.isZap(vira)) && cards.stream().anyMatch(card -> card.isCopas(vira));
        return hasCasalMaior;
    }

    static Boolean hasCasalMenor(List<TrucoCard> cards, TrucoCard vira) {
        Boolean hasCasalMenor = cards.stream().anyMatch(card -> card.isOuros(vira)) && cards.stream().anyMatch(card -> card.isEspadilha(vira));
        return hasCasalMenor;
    }

    static TrucoCard getStrongestCard(List<TrucoCard> cards, TrucoCard vira) {
        TrucoCard strongestCard = cards.get(0);
        for (TrucoCard card : cards) {
          if (card.compareValueTo(strongestCard, vira) > 0) strongestCard = card;
        }

        return strongestCard;
    }
}
