/*
 *  Copyright (C) 2023 Adriann Paranhos - IFSP/SCL and Emanuel José da Silva - IFSP/SCL
 *  Contact: adriann <dot> paranhos <at> aluno <dot> ifsp <dot> edu <dot> br
 *  Contact: emanuel <dot> silva <at> aluno <dot> ifsp <dot> edu <dot> br
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

package com.adriann.emanuel.armageddon;

import com.bueno.spi.model.*;
import com.bueno.spi.service.BotServiceProvider;

import java.util.Comparator;
import java.util.List;

import static com.bueno.spi.model.CardRank.*;

public class Armageddon implements BotServiceProvider {
    private static final int GOOD_HAND_STRENGTH = 25;
    @Override
    public boolean getMaoDeOnzeResponse(GameIntel intel) {
        TrucoCard vira = intel.getVira();
        List<TrucoCard> botCards = intel.getCards();
        if (!hasThree(botCards,vira) && !hasManilha(botCards,vira)) return false;

        int goodCard = 0;
        for (TrucoCard card: botCards){
            if (isTwo(vira) && isTwo(card)){
                goodCard++;
            }
            if (card.isManilha(vira) || isThree(card)){
                goodCard++;
            }
        }

        return goodCard >=2;
    }

    @Override
    public boolean decideIfRaises(GameIntel intel) {
        return false;
    }

    @Override
    public CardToPlay chooseCard(GameIntel intel) {
        int rounds = intel.getRoundResults().size();

        TrucoCard vira = intel.getVira();
        List<TrucoCard> botCards = intel.getCards();

        switch (rounds) {
            case 0 -> {
                if (hasHigherCouple(botCards, vira)) {
                    return CardToPlay.of(weakestCard(botCards, vira));
                }
                if (hasManilha(botCards, vira)){
                    return CardToPlay.of(middleCard(botCards, vira));
                }
                if (handStrength(botCards, vira) < GOOD_HAND_STRENGTH) {
                    return CardToPlay.of(strongestCard(botCards, vira));
                }
                if (handStrength(botCards, vira) > GOOD_HAND_STRENGTH) {
                    return CardToPlay.of(middleCard(botCards,vira));
                }
                if (intel.getOpponentCard().get().relativeValue(vira) == botCards.get(0).relativeValue(vira)){
                    return CardToPlay.of(botCards.get(0));
                }
            }
        }

        return CardToPlay.of(botCards.get(0));
    }

    @Override
    public int getRaiseResponse(GameIntel intel) {
        return 0;
    }

    private boolean hasManilha(List<TrucoCard> cards,TrucoCard vira){
        for (TrucoCard card:cards){
            if (card.isManilha(vira)) return true;
        }
        return false;
    }

    private boolean hasThree(List<TrucoCard> cards,TrucoCard vira){
        for (TrucoCard card:cards){
            if (card.compareValueTo(TrucoCard.of(CardRank.THREE, CardSuit.DIAMONDS), vira) == 0){
                return true;
            }
        }
        return false;
    }

    private boolean isThree(TrucoCard card){
        return card.getRank().equals(THREE);
    }

    private boolean isTwo(TrucoCard card){
        return card.getRank().equals(TWO);
    }

    private boolean hasHigherCouple(List<TrucoCard> cards, TrucoCard vira){
        int score = 0;
        for (TrucoCard card:cards){
            if (card.isCopas(vira)) score++;
            if (card.isZap(vira)) score++;
        }

        return score == 2;
    }

    private TrucoCard weakestCard(List<TrucoCard> cards, TrucoCard vira){
        TrucoCard weakest = cards.get(0);
        for (TrucoCard card:cards){
            if (weakest.compareValueTo(card,vira) > 0) weakest = card;
        }

        return weakest;
    }

    private TrucoCard strongestCard(List<TrucoCard> cards, TrucoCard vira){
        TrucoCard strongest = cards.get(0);
        for (TrucoCard card:cards){
            if (strongest.compareValueTo(card,vira) < 0) strongest = card;
        }

        return strongest;
    }

    private TrucoCard middleCard(List<TrucoCard> cards, TrucoCard vira){
        List<TrucoCard> sortedCarts = cards.stream().
                sorted(Comparator.comparingInt(card -> card.relativeValue(vira)))
                .toList();

        return sortedCarts.get(1);
    }

    private int handStrength(List<TrucoCard> cards, TrucoCard vira){
        int strength = 0;
        for (TrucoCard card:cards){
            strength += card.relativeValue(vira);
        }

        return strength;
    }
}
