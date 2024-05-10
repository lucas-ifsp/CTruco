package com.contiero.lemes.newbot.services.analise;

import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;
import com.contiero.lemes.newbot.interfaces.Analise;
import com.contiero.lemes.newbot.services.utils.PowerCalculatorService;

import java.util.List;
import java.util.Optional;

public class AnaliseWhileLosing implements Analise {

    private final GameIntel intel;

    public AnaliseWhileLosing(GameIntel intel) {
        this.intel = intel;
    }

    @Override
    public HandStatus myHand() {
        List<TrucoCard> myCards = intel.getCards();
        if (myCards.size() == 3){
            return threeCardsHandler(myCards);
        }
        if (myCards.size() == 2){
            return twoCardsHandler(myCards);
        }
        if(myCards.size() == 1){
            return oneCardHandler();
        }
        return HandStatus.GOD;
    }

    private HandStatus threeCardsHandler(List<TrucoCard> myCards){
        if (haveAtLeastTwoManilhas()){
            return HandStatus.GOD;
        }
        if (haveAtLeastOneManilha()){
            if(intel.getOpponentCard().isPresent()){
                TrucoCard oppCard = intel.getOpponentCard().get();
                if(myCards
                        .stream()
                        .filter(card -> card.compareValueTo(oppCard,intel.getVira()) > 0)
                        .count() == 3){
                    return HandStatus.GOD;
                }
            }
            if (PowerCalculatorService.powerOfCard(intel,1) >= 3) return HandStatus.GOOD;
            return HandStatus.MEDIUM;
        }
        long handPower = powerOfTheTwoBestCards();
        if (handPower >= 13){
            return HandStatus.GOOD;
        }
        if (handPower >= 8) {
            return HandStatus.MEDIUM;
        }
        return HandStatus.BAD;
    }

    private HandStatus twoCardsHandler(List<TrucoCard> myCards){
        if (wonFirstRound()){
            if(PowerCalculatorService.powerOfCard(intel,0) >= 9){
                return HandStatus.GOD;
            }
            if (PowerCalculatorService.powerOfCard(intel,0) >= 5){
                return HandStatus.GOOD;
            }
            return HandStatus.MEDIUM;
        }
        if (lostFirstRound()){

            if(intel.getOpponentCard().isPresent()){
                TrucoCard oppCard = intel.getOpponentCard().get();
                if(myCards
                        .stream()
                        .filter(card -> card.compareValueTo(oppCard,intel.getVira()) > 0)
                        .count() == 2){
                    return HandStatus.GOOD;
                }
                return HandStatus.MEDIUM;
            }

            if (haveAtLeastTwoManilhas()) return HandStatus.GOD;

            if (haveAtLeastOneManilha()){
                if (PowerCalculatorService.powerOfCard(intel,1) >= 7) return HandStatus.GOD;
                if (PowerCalculatorService.powerOfCard(intel,1) >= 4) return HandStatus.GOOD;
                return HandStatus.MEDIUM;
            }
            if (powerOfTheTwoBestCards() >= 13) return HandStatus.GOOD;
            if (powerOfTheTwoBestCards() >= 10) return HandStatus.MEDIUM;
            return HandStatus.BAD;
        }

        if (haveAtLeastOneManilha()) return HandStatus.GOD;
        if (PowerCalculatorService.powerOfCard(intel,0) >= 8) return HandStatus.GOOD;
        if (PowerCalculatorService.powerOfCard(intel,0) >= 5) return HandStatus.MEDIUM;
        return HandStatus.BAD;
    }

    private HandStatus oneCardHandler(){
        TrucoCard myCard = intel.getCards().get(0);
        Optional<TrucoCard> oppCard = intel.getOpponentCard();

        if (wonFirstRound()){
            if (oppCard.isPresent()){
                if (myCard.compareValueTo(oppCard.get(),intel.getVira()) > 0){
                    return HandStatus.GOD;
                }
                if (intel.getHandPoints() <= 6){
                    return HandStatus.GOOD;
                }
                return HandStatus.MEDIUM;
            }
        }

        if (lostFirstRound()){
            if (PowerCalculatorService.powerOfCard(intel,0) >= 9) return HandStatus.GOD;
            if (PowerCalculatorService.powerOfCard(intel,0) >= 7) return HandStatus.GOOD;
            if (PowerCalculatorService.powerOfCard(intel,0) >= 3) return HandStatus.MEDIUM;
            return HandStatus.BAD;
        }

        if (intel.getHandPoints() <= 3) return HandStatus.GOD;
        if (PowerCalculatorService.powerOfCard(intel,0) >= 5) return HandStatus.GOOD;
        if (PowerCalculatorService.powerOfCard(intel,0) >= 3) return HandStatus.MEDIUM;
        return HandStatus.BAD;
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
                .mapToLong(card -> card.relativeValue(intel.getVira()))
                .sorted()
                .limit(2)
                .sum();
    }

    private boolean wonFirstRound(){
        if (!intel.getRoundResults().isEmpty()) return intel.getRoundResults().get(0) == GameIntel.RoundResult.WON;
        return false;
    }

    private boolean lostFirstRound(){
        if (!intel.getRoundResults().isEmpty()) return intel.getRoundResults().get(0) == GameIntel.RoundResult.LOST;
        return false;
    }
}
