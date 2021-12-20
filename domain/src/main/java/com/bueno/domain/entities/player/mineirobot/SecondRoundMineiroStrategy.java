/*
 *  Copyright (C) 2021 Lucas B. R. de Oliveira - IFSP/SCL
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

package com.bueno.domain.entities.player.mineirobot;


import com.bueno.domain.entities.deck.Card;
import com.bueno.domain.entities.player.util.CardToPlay;
import com.bueno.domain.entities.player.util.Player;

import java.util.List;
import java.util.Optional;

public class SecondRoundMineiroStrategy extends PlayingStrategy {

    public SecondRoundMineiroStrategy(List<Card> cards, Player player) {
        this.cards = cards;
        this.player = player;
        this.intel = player.getIntel();
        this.vira = intel.vira();
    }

    @Override
    public CardToPlay playCard() {
        cards.sort((c1, c2) -> c2.compareValueTo(c1, vira));
        final Optional<Card> possibleOpponentCard = intel.cardToPlayAgainst();
        final Optional<String> possibleFirstRoundWinner = intel.roundWinners().get(0);

        if (isPlayerFirstRoundWinner(possibleFirstRoundWinner.orElse(null))) {
            if (cards.stream().anyMatch(c -> getCardValue(c, vira) >= 8)) return CardToPlay.ofDiscard(cards.get(1));
            return CardToPlay.of(cards.get(0));
        }

        if (isFirstRoundTied(possibleFirstRoundWinner.orElse(null))) return CardToPlay.of(cards.get(0));

        Optional<Card> enoughCardToWin = getPossibleEnoughCardToWin(possibleOpponentCard.orElseThrow());
        return enoughCardToWin.map(CardToPlay::of).orElseGet(() -> CardToPlay.ofDiscard(cards.get(1)));
    }

    @Override
    public int getTrucoResponse(int newScoreValue) {
        cards.sort((c1, c2) -> c2.compareValueTo(c1, vira));
        final Optional<String> possibleFirstRoundWinner = intel.roundWinners().get(0);
        final int bestCardValue = getCardValue(cards.get(0), vira);

        if (isFirstRoundTied(possibleFirstRoundWinner.orElse(null))) {
            if (bestCardValue < 10) return -1;
            if (bestCardValue > 11) return 1;
        }

        if (isPlayerFirstRoundLoser(possibleFirstRoundWinner.orElse(null)) && hasAlreadyPlayedRound()) {
            if (bestCardValue < 10) return -1;
            if (bestCardValue > 11) return 1;
        }

        if (isPlayerFirstRoundLoser(possibleFirstRoundWinner.orElse(null)) && !hasAlreadyPlayedRound()) {
            final int remainingCardsValue = getCardValue(cards.get(0), vira) + getCardValue(cards.get(1), vira);
            if (remainingCardsValue < 18 || (newScoreValue >= 6 && remainingCardsValue < 20)) return -1;
            if (remainingCardsValue >= 23) return 1;
        }
        return 0;
    }

    private boolean hasAlreadyPlayedRound() {
        return cards.size() == 1;
    }

    @Override
    public boolean requestTruco() {
        cards.sort((c1, c2) -> c2.compareValueTo(c1, vira));
        final Optional<String> possibleFirstRoundWinner = intel.roundWinners().get(0);
        final Optional<Card> possibleOpponentCard = intel.cardToPlayAgainst();
        final int handScoreValue = intel.handScore().get();
        final Card higherCard = cards.get(0);

        if (isPlayerFirstRoundWinner(possibleFirstRoundWinner.orElse(null))) return false;

        if (isFirstRoundTied(possibleFirstRoundWinner.orElse(null))){
            if(isCardValueBetween(higherCard,13,13)
                    || isAbleToWinWith(higherCard, possibleOpponentCard.orElse(null))) return true;
            if(handScoreValue == 1 && isCardValueBetween(higherCard, 10, 12)) return true;
            if(handScoreValue == 3 && isCardValueBetween(higherCard, 12, 12)) return true;
        }

        if(handScoreValue > 1) return false;

        if (isAbleToWinWith(higherCard, possibleOpponentCard.orElse(null))
                && isCardValueBetween(getThirdRoundCard(possibleOpponentCard.orElse(null)),
                10, 13)) return true;

        return false;
    }

    private boolean isCardValueBetween(Card card, int lowerValue, int upperValue){
        final int value = getCardValue(card, vira);
        return lowerValue <= value &&  value <= upperValue;
    }

    private boolean isAbleToWinWith(Card playingCard, Card possibleOpponentCard) {
        return possibleOpponentCard != null && playingCard.compareValueTo(possibleOpponentCard, vira) > 0;
    }

    private Card getThirdRoundCard(Card possibleOpponentCard){
        cards.sort((c1, c2) -> c2.compareValueTo(c1, vira));
        final Card higherCard = cards.get(0);
        final Card worstCard = cards.get(1);
        if(isAbleToWinWith(worstCard, possibleOpponentCard)) return higherCard;
        return worstCard;
    }
}