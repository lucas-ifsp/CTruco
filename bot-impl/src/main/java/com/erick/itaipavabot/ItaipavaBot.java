/*
 *  Copyright (C) 2024 Erick Santinon Gomes - IFSP/SCL
 *  Contact: santinon <dot> gomes <at> aluno <dot> ifsp <dot> edu <dot> br
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

package com.erick.itaipavabot;

import com.bueno.spi.model.*;
import com.bueno.spi.service.BotServiceProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

public class ItaipavaBot implements BotServiceProvider {

    private CardToPlay firstRound(GameIntel gameIntel) {
        List<TrucoCard> myCards = gameIntel.getCards();
        if (findFirstPlayer(gameIntel)) {
            return CardToPlay.of(getHighestCard(myCards, gameIntel));
        }
        TrucoCard card = findLowestCardToWin(gameIntel);
        if(card == null) {
            return CardToPlay.of(getLowestCard(myCards, gameIntel));
        } else {
            return CardToPlay.of(findLowestCardToWin(gameIntel));
        }
    }

    @Override
    public boolean getMaoDeOnzeResponse(GameIntel intel) {
        if (findHowManyManilhas(intel) == 3) return true;
        if (findHowManyManilhas(intel) == 2 && hasCard(intel, true)) return true;
        return false;
    }

    @Override
    public boolean decideIfRaises(GameIntel intel) {
        if (findHowManyManilhas(intel) == 3) return true;
        if (handPowerLevel(intel) >= 9) return true;
        return false;
    }

    @Override
    public CardToPlay chooseCard(GameIntel intel) {
        return firstRound(intel);
    }

    @Override
    public int getRaiseResponse(GameIntel intel) {
        if (hasCasalMaior(intel)) return 1;
        if (findHowManyManilhas(intel) == 2) return 0;
        if (hasCard(intel, true) && hasCard(intel, TrucoCard.of(CardRank.THREE, CardSuit.CLUBS))) return 0;
        return -1;
    }

    private TrucoCard findLowestCardToWin(GameIntel gameIntel) {
        List<TrucoCard> myCards = gameIntel.getCards();
        TrucoCard vira = gameIntel.getVira();
        TrucoCard highestCard = getHighestCard(gameIntel);
        TrucoCard lowestWinningCard = null;
        for (TrucoCard card : myCards) {
            if (card.relativeValue(vira) > highestCard.relativeValue(vira)) {
                if (lowestWinningCard == null || card.relativeValue(vira) < lowestWinningCard.relativeValue(vira)) {
                    lowestWinningCard = card;
                }
            }
        }
        return lowestWinningCard;
    }


    private TrucoCard getHighestCard(GameIntel gameIntel) {
        TrucoCard highestCard = gameIntel.getOpenCards().get(0);
        highestCard = getTrucoCard(gameIntel.getOpenCards(), gameIntel, highestCard);
        return highestCard;
    }

    private TrucoCard getHighestCard(List<TrucoCard> myCards, GameIntel gameIntel) {
        TrucoCard highestCard = myCards.get(0);
        highestCard = getTrucoCard(myCards, gameIntel, highestCard);
        return highestCard;
    }

    private static TrucoCard getTrucoCard(List<TrucoCard> myCards, GameIntel gameIntel, TrucoCard highestCard) {
        for(TrucoCard card : myCards) {
            if (card.relativeValue(gameIntel.getVira()) > highestCard.relativeValue(gameIntel.getVira())) {
                highestCard = card;
            }
        }
        return highestCard;
    }

    private TrucoCard getLowestCard(List<TrucoCard> myCards, GameIntel gameIntel) {
        if (myCards == null || myCards.isEmpty()) {
            throw new IllegalArgumentException("The card list cannot be null or empty.");
        }

        TrucoCard lowestCard = myCards.get(0);
        for (TrucoCard card : myCards) {
            if (card.relativeValue(gameIntel.getVira()) < lowestCard.relativeValue(gameIntel.getVira())) {
                lowestCard = card;
            }
        }
        return lowestCard;
    }

    private int findHowManyManilhas(GameIntel gameIntel) {
        List<TrucoCard> myCards = gameIntel.getCards();
        int counter = 0;
        for (TrucoCard card : myCards) {
            if (card.isManilha(gameIntel.getVira())) {
                counter++;
            }
        }
        return counter;
    }

    public boolean hasCard(GameIntel gameIntel, TrucoCard card) {
        List<TrucoCard> myCards = gameIntel.getCards();
        return myCards.stream().anyMatch(cards -> cards.getRank() == card.getRank());
    }

    public boolean hasCard(GameIntel gameIntel, boolean hasZap) {
        List<TrucoCard> myCards = gameIntel.getCards();
        for (TrucoCard card : myCards) {
            if (card.isZap(gameIntel.getVira())) {
                return true;
            }
        }
        return false;
    }

    public boolean hasCasalMaior(GameIntel gameIntel) {
        List<TrucoCard> myCards = gameIntel.getCards();
        boolean hasManilhaClubs = myCards.stream()
                .anyMatch(card -> card.isManilha(gameIntel.getVira()) && card.getSuit() == CardSuit.CLUBS);
        boolean hasManilhaHearts = myCards.stream()
                .anyMatch(card -> card.isManilha(gameIntel.getVira()) && card.getSuit() == CardSuit.HEARTS);

        return hasManilhaClubs && hasManilhaHearts;
    }

    boolean findFirstPlayer(GameIntel intel){
        return intel.getOpponentCard().isEmpty();
    }

    public double handPowerLevel(GameIntel gameIntel) {
        List<TrucoCard> myCards = gameIntel.getCards();
        double powerLevel = 0;
        for (TrucoCard card : myCards) {
            powerLevel += card.relativeValue(gameIntel.getVira());
        }
        powerLevel = powerLevel/myCards.size();
        return powerLevel;
    }
}

