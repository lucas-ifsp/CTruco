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
import com.bueno.domain.entities.player.util.CardToPlay;
import com.bueno.domain.usecases.bot.spi.GameIntel;
import com.bueno.domain.usecases.bot.spi.GameIntel.RoundResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.bueno.domain.usecases.bot.impl.PlayingStrategy.*;

public class SecondRoundStrategy implements PlayingStrategy {

    private final List<Card> cards;
    private final Card vira;
    private final GameIntel intel;
    private final List<Card> openCards;

    public SecondRoundStrategy(GameIntel intel) {
        this.intel = intel;
        this.vira = intel.getVira();
        this.cards = new ArrayList<>(intel.getCards());
        this.cards.sort((c1, c2) -> c2.compareValueTo(c1, vira));
        this.openCards = intel.getOpenCards();
    }

    @Override
    public CardToPlay chooseCard() {
        cards.sort((c1, c2) -> c2.compareValueTo(c1, vira));
        final Optional<Card> possibleOpponentCard = intel.getOpponentCard();
        final RoundResult firstRoundResult = intel.getRoundResults().get(0);

        if (firstRoundResult.equals(RoundResult.WON)) {
            if (!isMaoDeFerro() && cards.stream().anyMatch(c -> getCardValue(openCards, c, vira) >= 8) )
                return CardToPlay.ofDiscard(cards.get(1));
            return CardToPlay.of(cards.get(0));
        }

        if (firstRoundResult.equals(RoundResult.DREW)) return CardToPlay.of(cards.get(0));

        Optional<Card> enoughCardToWin = getPossibleEnoughCardToWin(cards, vira, possibleOpponentCard.orElseThrow());
        return enoughCardToWin.map(CardToPlay::of).orElseGet(() -> CardToPlay.ofDiscard(cards.get(1)));
    }

    private boolean isMaoDeFerro() {
        return intel.getScore() == 11 && intel.getOpponentScore() == 11;
    }

    @Override
    public int getRaiseResponse(int newScoreValue) {
        cards.sort((c1, c2) -> c2.compareValueTo(c1, vira));
        final RoundResult firstRoundResult = intel.getRoundResults().get(0);
        final int bestCardValue = getCardValue(openCards, cards.get(0), vira);

        if(canWin(cards.get(0), firstRoundResult)) return 1;

        if (firstRoundResult.equals(RoundResult.DREW)) {
            if (bestCardValue < 10) return -1;
            if (bestCardValue > 11) return 1;
        }

        if (firstRoundResult.equals(RoundResult.LOST) && hasAlreadyPlayedRound() && bestCardValue < 10) return -1;

        if (firstRoundResult.equals(RoundResult.LOST)  && !hasAlreadyPlayedRound()) {
            final int remainingCardsValue = getCardValue(openCards, cards.get(0), vira) + getCardValue(openCards, cards.get(1), vira);
            if (remainingCardsValue < 18 || (newScoreValue >= 6 && remainingCardsValue < 20)) return -1;
            if (remainingCardsValue >= 23) return 1;
        }
        return 0;
    }

    private boolean canWin(Card bestCard, RoundResult firstRoundResult) {
        if(cards.size() < 2) return false;
        if(intel.getOpponentCard().isEmpty()) return false;
        final Card opponentCard = intel.getOpponentCard().get();
        return !firstRoundResult.equals(RoundResult.LOST) && bestCard.compareValueTo(opponentCard, vira) > 0;
    }

    private boolean hasAlreadyPlayedRound() {
        return cards.size() == 1;
    }

    @Override
    public boolean decideIfRaises() {
        cards.sort((c1, c2) -> c2.compareValueTo(c1, vira));
        final RoundResult firstRoundResult = intel.getRoundResults().get(0);
        final Optional<Card> possibleOpponentCard = intel.getOpponentCard();
        final int handPoints = intel.getHandPoints();
        final Card higherCard = cards.get(0);

        if (firstRoundResult.equals(RoundResult.WON)) return false;

        if (firstRoundResult.equals(RoundResult.DREW)){
            if(isCardValueBetween(higherCard,13,13)
                    || isAbleToWinWith(higherCard, possibleOpponentCard.orElse(null))) return true;
            if(handPoints == 1 && isCardValueBetween(higherCard, 10, 12)) return true;
            if(handPoints == 3 && isCardValueBetween(higherCard, 12, 12)) return true;
        }

        if(handPoints > 1) return false;

        return isAbleToWinWith(higherCard, possibleOpponentCard.orElse(null))
                && isCardValueBetween(getThirdRoundCard(possibleOpponentCard.orElse(null)), 10, 13);
    }

    private boolean isCardValueBetween(Card card, int lowerValue, int upperValue){
        final int value = getCardValue(openCards, card, vira);
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