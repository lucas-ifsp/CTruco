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

package com.bueno.domain.usecases.bot.impl;

import com.bueno.domain.entities.deck.Card;
import com.bueno.domain.entities.game.Intel;
import com.bueno.domain.entities.player.util.CardToPlay;
import com.bueno.domain.entities.player.util.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

import static com.bueno.domain.usecases.bot.impl.PlayingStrategy.getCardValue;

public class FirstRoundStrategy implements PlayingStrategy {
    
    private final List<Card> cards;
    private final Card vira;
    private final Intel intel;
    private final List<Card> openCards;

    public FirstRoundStrategy(Player player, Intel intel) {
        this.intel = intel;
        this.vira = intel.vira();
        this.cards = new ArrayList<>(player.getCards());
        this.cards.sort((c1, c2) -> c2.compareValueTo(c1, vira));
        this.openCards = intel.openCards();
    }

    @Override
    public CardToPlay chooseCard() {
        final Optional<Card> possibleOpponentCard = intel.cardToPlayAgainst();
        final int numberOfTopThreeCards = countCardsBetween(11, 13);
        final int numberOfMediumCards = countCardsBetween(8,9);

        if(numberOfTopThreeCards == 2) return CardToPlay.ofDiscard(cards.get(2));

        if(possibleOpponentCard.isPresent()) {
            Card opponentCard = possibleOpponentCard.get();

            Optional<Card> possibleEnoughCardToWin = getPossibleEnoughCardToWin(cards, vira, opponentCard);
            if (possibleEnoughCardToWin.isPresent()) return CardToPlay.of(possibleEnoughCardToWin.get());

            Optional<Card> possibleCardToDraw = getPossibleCardToDraw(cards, vira, opponentCard);
            return possibleCardToDraw.map(CardToPlay::of).orElseGet(() -> CardToPlay.ofDiscard((cards.get(2))));
        }
        if(numberOfTopThreeCards == 1 && numberOfMediumCards > 0) return CardToPlay.of(cards.get(1));
        return CardToPlay.of(cards.get(0));
    }

    private int countCardsBetween(int lowerValue, int upperValue){
        Predicate<Card> hasValueHigherOrEqualThan = c ->  lowerValue <= getCardValue(openCards, c, vira);
        Predicate<Card> hasValueLowerOrEqualThan = c ->  getCardValue(openCards, c, vira) <= upperValue;
        return (int) cards.stream()
                .filter(hasValueHigherOrEqualThan)
                .filter(hasValueLowerOrEqualThan)
                .count();
    }

    @Override
    public int getRaiseResponse(int newScoreValue) {
        final int remainingCardsValue = getCardValue(openCards, cards.get(0), vira) + getCardValue(openCards, cards.get(1), vira);
        if(cards.size() == 3) {
            if (remainingCardsValue >= 23) return 1;
            if (!cards.get(0).isManilha(vira) || cards.get(0).isOuros(vira) || getCardValue(openCards, cards.get(1), vira) < 9) return -1;
            return 0;
        }
        final List<Card> openCards = this.openCards;
        final Card cardPlayed = openCards.get(openCards.size()-1);
        if(getCardValue(this.openCards, cardPlayed, vira) < 9 && remainingCardsValue < 17) return -1;
        return 0;
    }

    @Override
    public boolean decideIfRaises() {
        return false;
    }
}
