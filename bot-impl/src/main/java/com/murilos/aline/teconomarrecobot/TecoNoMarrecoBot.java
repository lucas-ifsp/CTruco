package com.murilos.aline.teconomarrecobot;

import com.bueno.spi.model.CardToPlay;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;
import com.bueno.spi.service.BotServiceProvider;

import java.util.List;

public class TecoNoMarrecoBot implements BotServiceProvider {

    @Override
    public boolean getMaoDeOnzeResponse(GameIntel intel) {
        TrucoCard cardVira = intel.getVira();
        List<TrucoCard> cards = intel.getCards();
        if(possuiCasalMaior(intel)){
            return true;
        }
        if(possuiMaoTres(intel)){
            return true;
        }
        if(manilhaCount(cards, cardVira) == 2){
            return true;
        }
        if(valueOfTheHand(intel) > 20){
            return true;
        }
        if(intel.getOpponentScore() < 4){
            return true;
        }
        return false;

    }



    @Override
    public boolean decideIfRaises(GameIntel intel) {
        return false;
    }

    @Override
    public CardToPlay chooseCard(GameIntel intel) {
        List<TrucoCard> card = intel.getCards();

        return CardToPlay.of(card.get(0));

    }

    @Override
    public int getRaiseResponse(GameIntel intel) {return 0;}

    private boolean possuiCasalMaior(GameIntel intel) {
        List<TrucoCard> cards = intel.getCards();
        TrucoCard cardVira = intel.getVira();

        for(int i = 0; i < 3; i++){
            if (cards.get(i).isZap(cardVira)) {
                for (int k = 0; k < 3; k++) {
                    if (cards.get(k).isCopas(cardVira)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean possuiMaoTres(GameIntel intel){
        Integer contador = 0;
        for(TrucoCard card : intel.getCards()){
            if(card.getRank().value() == 10){
                contador += 1;
            }
        }
        if(contador == 3){
            return true;
        }
        return  false;
    }

    private int manilhaCount(List<TrucoCard> cards, TrucoCard vira){
        int manilhaCount = 0;
        for (TrucoCard card : cards) {
            if (card.isManilha(vira)) {
                manilhaCount++;
            }
        }
        return manilhaCount;
    }

    private int valueOfTheHand(GameIntel intel){
        Integer hand = 0;
        for(TrucoCard card : intel.getCards()){
            if(card.isManilha(intel.getVira())){
                hand += card.relativeValue(intel.getVira());
            }else{
                hand += card.getRank().value();
            }
        }
        return hand;
    }



}
