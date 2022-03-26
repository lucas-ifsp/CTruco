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

package com.bueno.impl.mineirobot;

import com.bueno.spi.model.CardToPlay;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

interface PlayingStrategy {

    static PlayingStrategy of(GameIntel intel) {
        final int roundsPlayed = intel.getRoundResults().size();
        return switch (roundsPlayed) {
            case 0 -> new FirstRoundStrategy(intel);
            case 1 -> new SecondRoundStrategy(intel);
            case 2 -> new ThirdRoundStrategy(intel);
            default -> throw new IllegalStateException("Illegal number of rounds to play: " + roundsPlayed + 1);
        };
    }

    CardToPlay chooseCard();

    int getRaiseResponse(int newScoreValue);

    boolean decideIfRaises();

    default boolean getMaoDeOnzeResponse(GameIntel intel) {
        final TrucoCard vira = intel.getVira();
        final List<TrucoCard> cards = new ArrayList<>(intel.getCards());
        cards.sort((c1, c2) -> c2.compareValueTo(c1, vira));
        final int bestCard = getCardValue(intel.getOpenCards(), cards.get(0), vira);
        final int mediumCard = getCardValue(intel.getOpenCards(), cards.get(1), vira);
        final int lowerCard = getCardValue(intel.getOpenCards(), cards.get(2), vira);
        final int opponentScore = intel.getOpponentScore();

        if (opponentScore < 8) {
            if (bestCard + mediumCard + lowerCard >= 24) return true;
            if (bestCard >= 9 && mediumCard + lowerCard >= 13) return true;
        }
        return bestCard > 9 && mediumCard + lowerCard >= 14;
    }

    default Optional<TrucoCard> getPossibleCardToDraw(List<TrucoCard> botCards, TrucoCard vira, TrucoCard opponentCard) {
        return botCards.stream()
                .filter(card -> card.compareValueTo(opponentCard, vira) == 0)
                .findAny();
    }

    default Optional<TrucoCard> getPossibleEnoughCardToWin(List<TrucoCard> botCards, TrucoCard vira, TrucoCard opponentCard) {
        return botCards.stream()
                .filter(card -> card.compareValueTo(opponentCard, vira) > 0)
                .min((c1, c2) -> c1.compareValueTo(c2, vira));
    }

    static int getCardValue(List<TrucoCard> openCards, TrucoCard card, TrucoCard vira) {
        final List<TrucoCard> cards = new ArrayList<>(openCards);

        final int higherManilhasAlreadyPlayed = (int) cards.stream()
                .filter(c -> c.compareValueTo(card, vira) >= 1)
                .filter(c -> c.isManilha(vira))
                .count();

        final int higherCardsAlreadyPlayedFourTimes = (int) openCards.stream()
                .filter(c -> c.compareValueTo(card, vira) >= 1)
                .filter(c -> !c.isManilha(vira))
                .collect(Collectors.groupingBy(TrucoCard::getRank, Collectors.counting()))
                .values().stream()
                .filter(occurrences -> occurrences == 4)
                .count();

        final int offset = higherManilhasAlreadyPlayed + higherCardsAlreadyPlayedFourTimes;

        if (card.isOuros(vira)) return 9 + offset;
        if (card.isEspadilha(vira)) return 10 + offset;
        if (card.isCopas(vira)) return 11 + offset;
        if (card.isZap(vira)) return 12;

        final int discount = card.getRank().value() > vira.getRank().value() ? 1 : 0;
        final int actualValue = card.getRank().value() - 1;
        return actualValue + offset - discount;
    }

    //4 0
    //5 1
    //6 2
    //7 3
    //Q 4
    //J 5
    //K 6
    //A 7
    //2 8
    //3 9
}
