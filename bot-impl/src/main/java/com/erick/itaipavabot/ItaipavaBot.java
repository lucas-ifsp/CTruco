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

import com.bueno.spi.model.CardRank;
import com.bueno.spi.model.CardToPlay;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;
import com.bueno.spi.service.BotServiceProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

public class ItaipavaBot implements BotServiceProvider {

    private CardToPlay firstRound(GameIntel gameIntel) {
        List<TrucoCard> myCards = gameIntel.getCards();
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
        if (findHowManyManilhas(intel) == 2 && hasZap(intel)) return true;
        return false;
    }

    @Override
    public boolean decideIfRaises(GameIntel intel) {
        if (findHowManyManilhas(intel) == 3) return true;
        return false;
    }

    @Override
    public CardToPlay chooseCard(GameIntel intel) {
        return firstRound(intel);
    }

    @Override
    public int getRaiseResponse(GameIntel intel) {
        if (findHowManyManilhas(intel) <= 3 && findHowManyManilhas(intel) >= 2) return 1;
        if (hasZap(intel) && hasThree(intel)) return 0;
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
        for(TrucoCard card : gameIntel.getOpenCards()) {
            if (card.relativeValue(gameIntel.getVira()) > highestCard.relativeValue(gameIntel.getVira())) {
                highestCard = card;
            }
        }
        return highestCard;
    }

    private TrucoCard getHighestCard(List<TrucoCard> myCards, GameIntel gameIntel) {
        TrucoCard highestCard = myCards.get(0);
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

    private boolean hasZap(GameIntel gameIntel) {
        List<TrucoCard> myCards = gameIntel.getCards();
        for (TrucoCard card : myCards) {
            if (card.isZap(gameIntel.getVira())) {
                return true;
            }
        }
        return false;
    }

    public boolean hasThree(GameIntel gameIntel) {
        List<TrucoCard> myCards = gameIntel.getCards();
        return myCards.stream().anyMatch(card -> card.getRank() == CardRank.THREE);
    }


}

