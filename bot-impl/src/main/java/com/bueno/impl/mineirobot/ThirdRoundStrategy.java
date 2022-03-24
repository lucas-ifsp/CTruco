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

import com.bueno.spi.model.TrucoCard;
import com.bueno.spi.model.CardToPlay;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.GameIntel.RoundResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.bueno.impl.mineirobot.PlayingStrategy.getCardValue;

public class ThirdRoundStrategy implements PlayingStrategy {

    private final List<TrucoCard> cards;
    private final TrucoCard vira;
    private final GameIntel intel;
    private final List<TrucoCard> openCards;

    public ThirdRoundStrategy(GameIntel intel) {
        this.intel = intel;
        this.vira = intel.getVira();
        this.cards = new ArrayList<>(intel.getCards());
        this.cards.sort((c1, c2) -> c2.compareValueTo(c1, vira));
        this.openCards = intel.getOpenCards();
    }

    @Override
    public CardToPlay chooseCard() {
        final Optional<TrucoCard> possibleOpponentCard = intel.getOpponentCard();
        final TrucoCard remainingCard = cards.get(0);

        if (possibleOpponentCard.isEmpty()) return CardToPlay.of(remainingCard);

        final TrucoCard opponentCard = possibleOpponentCard.get();
        if (remainingCard.compareValueTo(opponentCard, vira) >= 0) return CardToPlay.of(remainingCard);

        return CardToPlay.ofDiscard(remainingCard);
    }

    @Override
    public int getRaiseResponse(int newScoreValue) {
        if(knowsTheOdds() && canWin()) return 1;

        final TrucoCard lastOpenedCard = openCards.get(openCards.size() - 1);
        final int playerCardValue = cards.isEmpty() ?
                getCardValue(openCards, lastOpenedCard, vira) : getCardValue(openCards, cards.get(0), vira);

        if (playerCardValue == 13) return 1;
        if (playerCardValue < 9 || (playerCardValue < 12 && newScoreValue >= 6)) return -1;
        return 0;
    }

    private boolean knowsTheOdds() {
        return cards.size() == 1 && intel.getOpponentCard().isPresent();
    }


    @Override
    public boolean decideIfRaises() {
        final Optional<TrucoCard> possibleOpponentCard = intel.getOpponentCard();
        final int handPoints = intel.getHandPoints();
        final TrucoCard playingCard = cards.get(0);

        if (possibleOpponentCard.isEmpty()) {
            if (handPoints > 1 && getCardValue(openCards, playingCard, vira) >= 12) return true;
            return getCardValue(openCards, playingCard, vira) >= 10;
        }

        if (getCardValue(openCards, possibleOpponentCard.get(), vira) < 5) return true;

        return canWin();
    }

    private boolean canWin() {
        final RoundResult firstRoundResult = intel.getRoundResults().get(0);
        final TrucoCard card = cards.get(0);
        final TrucoCard opponentCard = intel.getOpponentCard().orElseThrow();

        final boolean didNotLostFirstAndWinsThird = !firstRoundResult.equals(RoundResult.LOST)
                && card.compareValueTo(opponentCard, vira) > 0;

        final boolean wonFirstAndCanDrawThird = firstRoundResult.equals(RoundResult.WON)
                && card.compareValueTo(opponentCard, vira) == 0;

        return didNotLostFirstAndWinsThird || wonFirstAndCanDrawThird;
    }
}
