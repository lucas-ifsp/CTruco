/*
 * Copyright (C) 2021 Lucas B. R. de Oliveira
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
 *  along with Foobar.  If not, see <https://www.gnu.org/licenses/>
 */

package com.bueno.truco.domain.entities.player.mineirobot;

import com.bueno.truco.domain.entities.deck.Card;
import com.bueno.truco.domain.entities.player.util.Player;
import com.bueno.truco.domain.entities.player.util.PlayingStrategy;

import java.util.List;
import java.util.Optional;

public class FirstRoundMineiroStrategy extends PlayingStrategy {

    public FirstRoundMineiroStrategy(List<Card> cards, Player player) {
        this.cards = cards;
        this.player = player;
        this.intel = player.getIntel();
        this.vira = intel.getVira();
        this.cards.sort((c1, c2) -> c2.compareValueTo(c1, vira));
    }

    @Override
    public Card playCard() {
        final Optional<Card> possibleOpponentCard = intel.getCardToPlayAgainst();
        final int numberOfTopThreeCards = countCardsBetween(11, 13);
        final int numberOfMediumCards = countCardsBetween(8,9);

        if(numberOfTopThreeCards == 2) return discard(cards.get(2));

        if(possibleOpponentCard.isPresent()) {
            Card opponentCard = possibleOpponentCard.get();

            Optional<Card> possibleEnoughCardToWin = getPossibleEnoughCardToWin(opponentCard);
            if (possibleEnoughCardToWin.isPresent()) return cards.remove(cards.indexOf(possibleEnoughCardToWin.get()));

            Optional<Card> possibleCardToDraw = getPossibleCardToDraw(opponentCard);
            if (possibleCardToDraw.isPresent()) return cards.remove(cards.indexOf(possibleCardToDraw.get()));

            return discard(cards.get(2));
        }
        if(numberOfTopThreeCards == 1 && numberOfMediumCards > 0) return cards.remove(1);
        return cards.remove(0);
    }

    private int countCardsBetween(int lowerValue, int upperValue){
        return (int) cards.stream()
                .filter(c ->  lowerValue <= getCardValue(c, vira) && getCardValue(c, vira) <= upperValue)
                .count();
    }

    @Override
    public int getTrucoResponse(int newScoreValue) {
        final int remainingCardsValue = getCardValue(cards.get(0), vira) + getCardValue(cards.get(1), vira);
        if(cards.size() == 3) {
            if (remainingCardsValue >= 23) return 1;
            if (!cards.get(0).isManilha(vira) || cards.get(0).isOuros(vira) || getCardValue(cards.get(1), vira) < 9) return -1;
            return 0;
        }
        final List<Card> openCards = intel.getOpenCards();
        final Card cardPlayed = openCards.get(openCards.size()-1);
        if(getCardValue(cardPlayed, vira) < 9 && remainingCardsValue < 17) return -1;
        return 0;
    }

    @Override
    public boolean requestTruco() {
        return false;
    }
}
