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

import com.bueno.spi.model.CardToPlay;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;
import com.bueno.spi.service.BotServiceProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ItaipavaBot implements BotServiceProvider {

    private CardToPlay firstRound(GameIntel gameIntel) {
        List<TrucoCard> myCards = gameIntel.getCards();
        TrucoCard vira = gameIntel.getVira();
        TrucoCard highestPlayedCard = getHighestCard(gameIntel);
        TrucoCard highestHandCard = getHighestCard(myCards, gameIntel);
        if(highestPlayedCard.relativeValue(vira) < highestHandCard.relativeValue(vira)) {
            TrucoCard card = findLowestCardToWin(gameIntel);
            if(card == null) {
                return CardToPlay.of()
            }
            return CardToPlay.of(findLowestCardToWin(gameIntel));
        }
        return CardToPlay.of(myCards.get(0));
    }

    @Override
    public boolean getMaoDeOnzeResponse(GameIntel intel) {
        return false;
    }

    @Override
    public boolean decideIfRaises(GameIntel intel) {
        return false;
    }

    @Override
    public CardToPlay chooseCard(GameIntel intel) {
        return firstRound(intel);
    }

    @Override
    public int getRaiseResponse(GameIntel intel) {
        return 0;
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

}
