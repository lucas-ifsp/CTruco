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

package com.zampieri.rissatti.impl.UncleBobBot;
import com.bueno.spi.model.CardRank;
import com.bueno.spi.model.CardToPlay;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;
import com.bueno.spi.service.BotServiceProvider;

import java.util.List;

public class UncleBobBot implements BotServiceProvider {

    @Override
    public int getRaiseResponse(GameIntel intel) { List<TrucoCard> cards = intel.getCards();
        TrucoCard vira = intel.getVira();
        List<GameIntel.RoundResult> result = intel.getRoundResults();

        int numberOfManilhas = 0;
        if ( cards.isEmpty()) {
            return -1;
        }
        TrucoCard lowestCard = cards.get(0);
        TrucoCard highest = cards.get(0);
        for ( TrucoCard card : cards) {
            if (card.isManilha(vira)) {
                numberOfManilhas++;
            }

            if (card.relativeValue(vira) < lowestCard.relativeValue(vira)) {
                lowestCard = card;
            }

            if (card.relativeValue(vira) > highest.relativeValue(vira)) {
                highest = card;
            }
        }

        if ( numberOfManilhas > 1) {
            return 1;
        }

        if ( numberOfManilhas == 1) {
            if ( !result.isEmpty() && (result.get(0) == GameIntel.RoundResult.DREW || result.get(0) == GameIntel.RoundResult.WON)) {
                return 1;
            }
            return 0;
        }
        //Jack represents de relative value = 6
        if( lowestCard.relativeValue(vira) < 6) {
            return -1;
        }
        if( highest.relativeValue(vira) > 8 && !result.isEmpty() && (result.get(0) == GameIntel.RoundResult.DREW || result.get(0) == GameIntel.RoundResult.WON)) {
            return 0;
        }
        if ( intel.getOpponentScore() >= 9) {
            return -1;
        }

        return -1;
    }

    @Override
    public boolean getMaoDeOnzeResponse(GameIntel intel) {
        if(CountManilhas(intel) >= 2){return true;}
        if(hasZap(intel)) return !AnyCardsWithValueLowerThanSix(intel);
        else{
            return false;
        }
    }

    @Override
    public boolean decideIfRaises(GameIntel intel) {
        if(!HandPointsEqualEleven(intel)){
            if(CountManilhas(intel) >= 2){
                return true;
            }
            else{
                if( !intel.getRoundResults().isEmpty() && intel.getRoundResults().get(0) == GameIntel.RoundResult.WON ) {
                    return AnyCardsWithValueHigherThanTwo(intel);
                }
                if(hasZap(intel)){
                    return AnyCardsWithValueHigherThanKing(intel);
                }
            }
        }
        return false;
    }

    @Override
    public CardToPlay chooseCard(GameIntel intel) {
        List<TrucoCard> cards = intel.getCards();
        TrucoCard vira = intel.getVira();
        List<GameIntel.RoundResult> result = intel.getRoundResults();

        int numberOfManilhas = 0;

        for ( TrucoCard card : cards) {
            if (card.isManilha(vira)) {
                numberOfManilhas ++;
            }
        }

        if ( !result.isEmpty() && result.get(0) == GameIntel.RoundResult.DREW) {
            return CardToPlay.of(getHighestCard(cards, vira));
        }

        if ( intel.getOpponentCard().isPresent()) {
           TrucoCard opponentCard = intel.getOpponentCard().get();

           if (opponentCard.getRank().equals(CardRank.HIDDEN)) {
               return CardToPlay.of(getLowestCard(cards,vira));
           }
           if ( opponentCard.isManilha(vira) && numberOfManilhas >= 1) {
               for (TrucoCard card : cards) {
                   if ( card.relativeValue(vira) > opponentCard.relativeValue(vira)) {
                       return CardToPlay.of(card);
                   }
               }
               return CardToPlay.of(getLowestCard(cards,vira));
           }
            if ( opponentCard.isManilha(vira) && numberOfManilhas == 0) {
               return CardToPlay.of(getLowestCard(cards,vira));
            }
            else {
                for (TrucoCard card : cards) {
                    if ( card.relativeValue(vira) >= opponentCard.relativeValue(vira)) {
                        return CardToPlay.of(card);
                    }
                }
                return CardToPlay.of(getLowestCard(cards,vira));
            }
        }
        TrucoCard lowestCard = cards.get(0);
        for( TrucoCard card : cards) {
            if( card.isOuros(vira)) {
                return CardToPlay.of(card);
            }

            if ( !card.isManilha(vira)) {
                if (card.relativeValue(vira) < lowestCard.relativeValue(vira)) {
                    lowestCard = card;
                }

                return CardToPlay.of(lowestCard);
            } else {
                if (intel.getHandPoints() >= 6) {
                    return CardToPlay.of(getHighestCard(cards, vira));
                }

                if (card.relativeValue(vira) > lowestCard.relativeValue(vira) && !card.isManilha(vira)) {
                    return CardToPlay.of(card);
                }
            }
        }

        return CardToPlay.of(intel.getCards().get(0));
    }

    public TrucoCard getLowestCard(List<TrucoCard> cards, TrucoCard vira){
        TrucoCard lowestCard = cards.get(0);
        for (TrucoCard trucoCard : cards) {
            if (trucoCard.relativeValue(vira) < lowestCard.relativeValue(vira)) {
                lowestCard = trucoCard;
            }
        }

        return lowestCard;
    }

    public TrucoCard getHighestCard(List<TrucoCard> cards, TrucoCard vira){
        TrucoCard highestCard = cards.get(0);
        for (TrucoCard card : cards) {
            if (card.relativeValue(vira) > highestCard.relativeValue(vira)) {
                highestCard = card;
            }
        }

        return highestCard;
    }

    public boolean hasZap(GameIntel intel) {
        return intel.getCards().stream().anyMatch(card -> card.isZap(intel.getVira()));
    }

    public boolean AnyCardsWithValueLowerThanSix(GameIntel intel) {
        return intel.getCards().stream().anyMatch(card -> card.relativeValue(intel.getVira()) < 3);
    }

    public long CountManilhas(GameIntel intel){
        return intel.getCards().stream().filter(card -> card.isManilha(intel.getVira())).count();
    }

    public boolean AnyCardsWithValueHigherThanKing(GameIntel intel) {
        return intel.getCards().stream().anyMatch(card -> card.relativeValue(intel.getVira()) > 7);
    }

    public boolean AnyCardsWithValueHigherThanTwo(GameIntel intel) {
        return intel.getCards().stream().anyMatch(card -> card.relativeValue(intel.getVira()) > 8);
    }

    public boolean HandPointsEqualEleven(GameIntel intel){
        return intel.getHandPoints() == 11;
    }
}
