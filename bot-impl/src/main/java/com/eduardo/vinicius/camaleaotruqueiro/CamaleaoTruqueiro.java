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
