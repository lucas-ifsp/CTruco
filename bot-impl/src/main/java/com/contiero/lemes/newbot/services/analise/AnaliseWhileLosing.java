package com.contiero.lemes.newbot.services.analise;

import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;
import com.contiero.lemes.newbot.interfaces.Analise;

import java.util.List;

public class AnaliseWhileLosing implements Analise {

    private final GameIntel intel;

    public AnaliseWhileLosing(GameIntel intel) {
        this.intel = intel;
    }

    @Override
    public HandStatus myHand() {
        List<TrucoCard> myCards = intel.getCards();
        if (myCards.size() == 3){
            return threeCardsHandler();
        }
        if (myCards.size() == 2){
            return twoCardsHandler();
        }
        else{
            return oneCardHandler();
        }
    }

    private HandStatus threeCardsHandler(){
        if (haveAtLeastTwoManilhas()){
            return HandStatus.GOD;
        }
        if (haveAtLeastOneManilha()){
            return HandStatus.GOOD;
        }
        else{
            long handPower = powerOfTheTwoBestCards();
            if (handPower >= 16){
                return HandStatus.GOOD;
            }
            if (handPower >= 9) {
                return HandStatus.MEDIUM;
            }
            return HandStatus.BAD;
        }
    }

    private HandStatus twoCardsHandler(){
        if (wonFirstRound()){
            long handPower = powerOfTheTwoBestCards();
            if (haveAtLeastOneManilha()){
                return HandStatus.GOD;
            }
            if(handPower >= 12){
                return HandStatus.GOOD ;
            }
            if (handPower >= 8){
                return HandStatus.MEDIUM;
            }
            return HandStatus.BAD;
        }
        return HandStatus.MEDIUM;
    }

    private HandStatus oneCardHandler(){
        TrucoCard myCard = intel.getCards().get(0);
        if(haveAtLeastOneManilha()){
            if(thrownBiggerCardsThen(myCard)){
                return HandStatus.GOD;
            }
            return HandStatus.GOOD;
        }
        if (myCard.relativeValue(intel.getVira()) >= 5){
            return HandStatus.MEDIUM;
        }
        return HandStatus.BAD;
    }

    private boolean thrownBiggerCardsThen(TrucoCard myCard) {
        return intel.getOpenCards().stream().anyMatch(card -> card.compareValueTo(myCard, intel.getVira()) >= 1);
    }

    private boolean haveAtLeastTwoManilhas(){
        return getManilhaAmount() >= 2;
    }

    private boolean haveAtLeastOneManilha(){
        return getManilhaAmount() >= 1;
    }

    private long getManilhaAmount() {
        List<TrucoCard> myCards = intel.getCards();
        return myCards.stream()
                .filter(card-> card.isManilha(intel.getVira()))
                .count();
    }

    private long powerOfTheTwoBestCards(){
        List<TrucoCard> myCards = intel.getCards();
        return myCards.stream()
                .sorted()
                .mapToLong(card -> card.relativeValue(intel.getVira()))
                .limit(2)
                .sum();
    }

    private boolean wonFirstRound(){
        return intel.getRoundResults().get(0) == GameIntel.RoundResult.WON;
    }
}
