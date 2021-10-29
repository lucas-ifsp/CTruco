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

package com.bueno.truco.domain.entities.player.util;

import com.bueno.truco.domain.entities.deck.Card;
import com.bueno.truco.domain.entities.game.GameRuleViolationException;
import com.bueno.truco.domain.entities.hand.Intel;
import com.bueno.truco.domain.entities.player.mineirobot.FirstRoundMineiroStrategy;
import com.bueno.truco.domain.entities.player.mineirobot.SecondRoundMineiroStrategy;
import com.bueno.truco.domain.entities.player.mineirobot.ThirdRoundMineiroStrategy;

import java.util.*;

public abstract class PlayingStrategy{

    protected List<Card> cards;
    protected Player player;
    protected Intel intel;
    protected Card vira;

    public static PlayingStrategy of(List<Card> hand, Player player){
        final int roundsPlayed = player.getIntel().getRoundsPlayed().size();
        return switch (roundsPlayed){
            case 0 -> new FirstRoundMineiroStrategy(hand, player);
            case 1 -> new SecondRoundMineiroStrategy(hand, player);
            case 2 -> new ThirdRoundMineiroStrategy(hand, player);
            default -> throw new GameRuleViolationException("Illegal number of rounds to play: " + roundsPlayed + 1);
        };
    }

    public abstract Card playCard();
    public abstract int getTrucoResponse(int newScoreValue);
    public abstract boolean requestTruco();

    public final boolean getMaoDeOnzeResponse(){
        cards.sort((c1, c2) -> c2.compareValueTo(c1, vira));
        final int bestCard = getCardValue(cards.get(0), vira);
        final int mediumCard = getCardValue(cards.get(1), vira);
        final int worstCard = getCardValue(cards.get(2), vira);
        final int opponentScore = intel.getOpponentScore(player);

        if(opponentScore < 8 && (bestCard + mediumCard + worstCard) > 28 ) return true;
        if(opponentScore >= 8 && bestCard > 10 && mediumCard + worstCard >= 15) return true;

        return false;
    }

    protected final Optional<Card> getPossibleCardToDraw(Card opponentCard) {
        return cards.stream().filter(card -> card.compareValueTo(opponentCard, vira) == 0).findAny();
    }

    protected Optional<Card> getPossibleEnoughCardToWin(Card opponentCard) {
        return cards.stream()
                .filter(card -> card.compareValueTo(opponentCard, vira) > 0)
                .sorted((c1, c2) -> c1.compareValueTo(c2, vira))
                .findFirst();
    }

    protected final  boolean isPlayerFirstRoundWinner(Optional<Player> possibleWinner) {
        return possibleWinner.isPresent() && possibleWinner.get().equals(player);
    }

    protected final  boolean isPlayerFirstRoundLoser(Optional<Player> possibleWinner) {
        return possibleWinner.isPresent() && !possibleWinner.get().equals(player);
    }

    protected final boolean isFirstRoundTied(Optional<Player> possibleWinner) {
        return possibleWinner.isEmpty();
    }

    protected final int getCardValue(Card card, Card vira){
        char rank = String.valueOf(card.getRank()).toUpperCase().toCharArray()[0];
        final List<Character> rankValues = new ArrayList<>(Arrays.asList('4', '5', '6', '7', 'Q', 'J', 'K', '1', '2', '3'));
        final List<Card> openCards = intel.getOpenCards();

        int higherManilhasAlreadyPlayed = (int) openCards.stream()
                .filter(c -> c.compareValueTo(card, vira) > 1)
                .filter(c -> c.isManilha(vira))
                .count();

        int higherCardsAlreadyPlayedFourTimes = (int) openCards.stream()
                .filter(c -> c.compareValueTo(card, vira) > 1)
                .filter(c -> Collections.frequency(openCards, c) == 4)
                .count();

        int offset = higherManilhasAlreadyPlayed + higherCardsAlreadyPlayedFourTimes;

        if(card.isOuros(vira)) return  10 + offset;
        if(card.isEspadilha(vira)) return  11 + offset;
        if(card.isCopas(vira)) return 12 + offset;
        if(card.isZap(vira)) return 13;

        return rankValues.indexOf(rank) + offset;
    }

    protected final Card discard(Card card){
        return player.discard(card);
    }
}
