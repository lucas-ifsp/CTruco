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

package com.bueno.bots.mineirobot;

import com.bueno.domain.entities.deck.Card;
import com.bueno.domain.entities.game.Intel;
import com.bueno.domain.entities.player.util.CardToPlay;
import com.bueno.domain.entities.player.util.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

interface PlayingStrategy{

    static PlayingStrategy of(Player bot, Intel intel){
        final int roundsPlayed = intel.roundsPlayed();
        return switch (roundsPlayed){
            case 0 -> new FirstRoundStrategy(bot, intel);
            case 1 -> new SecondRoundStrategy(bot, intel);
            case 2 -> new ThirdRoundStrategy(bot, intel);
            default -> throw new IllegalStateException("Illegal number of rounds to play: " + roundsPlayed + 1);
        };
    }

    CardToPlay chooseCard();
    int getRaiseResponse(int newScoreValue);
    boolean decideIfRaises();

    static boolean getMaoDeOnzeResponse(Player bot, Intel intel){
        final Card vira = intel.vira();
        final List<Card> cards = new ArrayList<>(bot.getCards());
        cards.sort((c1, c2) -> c2.compareValueTo(c1, vira));
        final int bestCard = getCardValue(intel.openCards(), cards.get(0), vira);
        final int mediumCard = getCardValue(intel.openCards(), cards.get(1), vira);
        final int opponentScore = intel.currentOpponentScore();

        if(bestCard + mediumCard >= 20 ) return true;
        return opponentScore >= 8 && bestCard > 10 && mediumCard >= 8;
    }

    default Optional<Card> getPossibleCardToDraw(List<Card> botCards, Card vira, Card opponentCard) {
        return botCards.stream()
                .filter(card -> card.compareValueTo(opponentCard, vira) == 0)
                .findAny();
    }

    default Optional<Card> getPossibleEnoughCardToWin(List<Card> botCards, Card vira, Card opponentCard) {
        return botCards.stream()
                .filter(card -> card.compareValueTo(opponentCard, vira) > 0)
                .min((c1, c2) -> c1.compareValueTo(c2, vira));
    }

    default boolean isPlayerFirstRoundWinner(String possibleWinner, String botUserName) {
        return possibleWinner != null && possibleWinner.equals(botUserName);
    }

    default boolean isPlayerFirstRoundLoser(String possibleWinner, String botUserName) {
        return possibleWinner != null && !possibleWinner.equals(botUserName);
    }

    default boolean isFirstRoundTied(String possibleWinner) {
        return possibleWinner == null;
    }

    static int getCardValue(List<Card> openCards, Card card, Card vira){
        final List<Card> cards = new ArrayList<>(openCards);

        final int higherManilhasAlreadyPlayed = (int) cards.stream()
                .filter(c -> c.compareValueTo(card, vira) > 1)
                .filter(c -> c.isManilha(vira))
                .count();

        final int higherCardsAlreadyPlayedFourTimes = (int) cards.stream()
                .filter(c -> c.compareValueTo(card, vira) > 1)
                .filter(c -> Collections.frequency(cards, c) == 4)
                .count();

        final int offset = higherManilhasAlreadyPlayed + higherCardsAlreadyPlayedFourTimes;

        if(card.isOuros(vira)) return  10 + offset;
        if(card.isEspadilha(vira)) return  11 + offset;
        if(card.isCopas(vira)) return 12 + offset;
        if(card.isZap(vira)) return 13;

        final int actualValue = card.getRank().value() - 1;
        return actualValue + offset;
    }
}
