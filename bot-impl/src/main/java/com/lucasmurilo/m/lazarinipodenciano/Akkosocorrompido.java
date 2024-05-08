/*
 *  Copyright (C) 2024 Lucas Lazarini and Murilo M. Podenciano
 *  Contact: lazarini <dot> lucas <at> aluno <dot> ifsp <dot> edu <dot> br
 *  Contact: murilo <dot> marques <at> aluno <dot> ifsp <dot> edu <dot> br
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

package com.lucasmurilo.m.lazarinipodenciano;

import java.util.List;

import com.bueno.spi.model.CardToPlay;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;
import com.bueno.spi.service.BotServiceProvider;

public class Akkosocorrompido implements BotServiceProvider {
    @Override
    public boolean getMaoDeOnzeResponse(GameIntel intel) {
        if(intel.getOpponentScore()>7){
            return false;
        }
        if (haveHighCardInHand(intel)) {
            return true;   
        }
        return false;
    }

    @Override
    public boolean decideIfRaises(GameIntel intel) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'decideIfRaises'");
    }

    @Override
    public CardToPlay chooseCard(GameIntel intel) {
        return switch (getCurrentRoundNumber(intel)) {
            case 1 -> chooseCardFirstRound(intel);
            case 2 -> chooseCardSecondRound(intel);
            case 3 -> chooseCardThirdRound(intel);
            default -> null;
        };
    }

    @Override
    public int getRaiseResponse(GameIntel intel) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getRaiseResponse'");
    }

    //low card
    public TrucoCard getLowestRankInHand(GameIntel intel) {
        List<TrucoCard> botCards = intel.getCards();
        TrucoCard vira = intel.getVira();

        TrucoCard lowestCard = botCards.get(0);
        for (TrucoCard card : botCards) {
          if (card.relativeValue(vira)< lowestCard.relativeValue(vira)) {
            lowestCard = card;
          }
        }
        return lowestCard;
      }
    //high card
    public TrucoCard getHighestRankInHand(GameIntel intel) {
        List<TrucoCard> botCards = intel.getCards();
        TrucoCard vira = intel.getVira();

        TrucoCard highestCard = botCards.get(0);
        for (TrucoCard card : botCards) {
          if (card.relativeValue(vira) > highestCard.relativeValue(vira)) {
            highestCard = card;
          }
        }
        return highestCard;
      }
    //manilha
    private boolean haveHighCardInHand(GameIntel intel) {
        for (TrucoCard card : intel.getCards()) {
            if (card.isManilha(intel.getVira())) {
                return true;
            }
            if (card.getRank().toString() == "THREE") {
                return true;
            }
        }
        return false;
    }

    //lowtowin
    public TrucoCard getLowestCardToWin(List<TrucoCard> botCards, TrucoCard opponentCard, TrucoCard vira){
        TrucoCard lowestCardToWin = botCards.get(0);

        for (TrucoCard trucoCard : botCards) {
            if (trucoCard.relativeValue(vira) > opponentCard.relativeValue(vira)) {
                if (trucoCard.relativeValue(vira) < lowestCardToWin.relativeValue(vira)) {
                    lowestCardToWin = trucoCard;
                }
            }
        }
        return lowestCardToWin;
    }

    public CardToPlay chooseCardFirstRound(GameIntel intel){
        if (haveHighCardInHand(intel)) {
            return CardToPlay.of(getHighestRankInHand(intel));
        }
        //se o oponente jogou primeiro
        //return CardToPlay.of(getLowestCardToWin(intel.getCards(), intel.getOpponentCard(), intel.getVira()));        
        return CardToPlay.of(intel.getCards().get(0));
    }

    public CardToPlay chooseCardSecondRound(GameIntel intel){

        return CardToPlay.of(intel.getCards().get(0));
    }

    public CardToPlay chooseCardThirdRound(GameIntel intel){
        return CardToPlay.of(intel.getCards().get(0));
    }
    
    public int getCurrentRoundNumber(GameIntel intel){
        return intel.getRoundResults().size()+ 1;
    }
}