/*
 *  Copyright (C) 2024 Eduardo D. Derisso - IFSP/SCL and Vinicius S. G. Oliveira - IFSP/SCL
 *  Contact: duarte <dot> derisso <at> ifsp <dot> edu <dot> br
 *  Contact: vinicius <dot> goncalves1 <at> ifsp <dot> edu <dot> br
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

package com.eduardo.vinicius.camaleaotruqueiro;

import com.bueno.spi.model.*;
import com.bueno.spi.service.BotServiceProvider;

import java.util.List;

public class CamaleaoTruqueiro implements BotServiceProvider {
    @Override
    public boolean getMaoDeOnzeResponse(GameIntel intel) {
        return RoundStrategy.of(intel).getMaoDeOnzeResponse(intel);

        /*List<TrucoCard> cards = intel.getCards();
        TrucoCard vira = intel.getVira();
        if(getNumberOfHighRankCards(cards,vira) >= 2 && numberOfManilhas(cards,vira) >= 1) return true;
        else if (intel.getOpponentScore() < 9 && getNumberOfHighRankCards(cards,vira) >= 1) return true;
        else return false;*/
    }

    @Override
    public boolean decideIfRaises(GameIntel intel) {
        return RoundStrategy.of(intel).decideIfRaises(intel);


        /*List<TrucoCard> cards = intel.getCards();
        TrucoCard vira = intel.getVira();
        List<TrucoCard> lowerCard = List.of(getLowestCard(cards,vira));
        if(isTheFirstRound(intel) && !theBotPlaysFirst(intel)){
            if((getNumberOfHighRankCards(cards,vira)>1 || (getNumberOfHighRankCards(cards,vira)==1 && getNumberOfMediumRankCards(cards,vira)>0) )  && !haveStrongestCard(intel,lowerCard).isEmpty()){
                return true;
            }
            else if(getNumberOfHighRankCards(cards,vira)>=2){
                return true;
            }
            else{
                return false;
            }
        }
        else if(getNumberOfHighRankCards(cards,vira)>=2){
            return true;
        }
        else if(getNumberOfHighRankCards(cards,vira)==1){
            return true;
        }
        else {
            return false;
        }*/
    }

    @Override
    public CardToPlay chooseCard(GameIntel intel) {
        return RoundStrategy.of(intel).chooseCard(intel);


        /*List<TrucoCard> cards = intel.getCards();
        TrucoCard vira = intel.getVira();
        if(isTheFirstRound(intel)) {
            if (theBotPlaysFirst(intel)) {
                if (getNumberOfHighRankCards(cards, vira) >= 2) {
                    TrucoCard playCard = getGreatestCard(cards, vira);
                    return CardToPlay.of(playCard);
                } else {
                    TrucoCard playCard = getLowestCard(cards, vira);
                    return CardToPlay.of(playCard);
                }
            }
            else if(haveStrongestCard(intel,cards).size()>=2){
                TrucoCard playCard = getLowestCard(haveStrongestCard(intel,cards),vira);
                return CardToPlay.of(playCard);
            }
            else if(getNumberOfHighRankCards(cards,vira)==1){
                if(!haveStrongestCard(intel, cards).isEmpty()){
                    TrucoCard playCard = getGreatestCard(cards,vira);
                    return  CardToPlay.of(playCard);
                }
                else{
                    TrucoCard playCard = getLowestCard(cards,vira);
                    return CardToPlay.of(playCard);
                }
            }
            else {
                TrucoCard card = getLowestCard(cards, vira);
                return CardToPlay.of(card);
            }
        }
        else if (isTheSecondRound(intel)) {
            if(drewFistRound(intel)){
                TrucoCard playCard = getGreatestCard(cards,vira);
                return CardToPlay.of(playCard);
            }
            else if(theBotPlaysFirst(intel)){
                if(getNumberOfHighRankCards(cards,vira)==2){
                    TrucoCard playCard = getGreatestCard(cards,vira);
                    return CardToPlay.of(playCard);
                }
                else{
                    TrucoCard playCard = getLowestCard(cards,vira);
                    return CardToPlay.of(playCard);
                }
            }
            else if(getNumberOfHighRankCards(cards,vira)==1){
                if(!haveStrongestCard(intel, cards).isEmpty()){
                    TrucoCard playCard = getGreatestCard(cards,vira);
                    return  CardToPlay.of(playCard);
                }
                else{
                    TrucoCard playCard = getLowestCard(cards,vira);
                    return CardToPlay.of(playCard);
                }
            }
            else {
                TrucoCard playCard = getLowestCard(cards,vira);
                return CardToPlay.of(playCard);
            }
        }
        else {
            TrucoCard card = getGreatestCard(cards,vira);
            return CardToPlay.of(card);
        }*/
    }

    @Override
    public int getRaiseResponse(GameIntel intel) {
        return RoundStrategy.of(intel).getRaiseResponse(intel);


        /*List<TrucoCard> cards = intel.getCards();
        TrucoCard vira = intel.getVira();
        if(numberOfManilhas(cards,vira)>1){
            return 1;
        }
        if(getNumberOfHighRankCards(cards,vira)>=2){
            return 0;
        }
        else if(!isWinning(intel.getScore(), intel.getOpponentScore()) && getNumberOfHighRankCards(cards,vira)==1){
            return 0;
        }
        else {
            return -1;
        }*/
    }
}
