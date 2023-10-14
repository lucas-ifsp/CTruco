package com.everton.ronaldo.arrebentabot;

import com.bueno.spi.model.CardToPlay;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;
import com.bueno.spi.service.BotServiceProvider;

import java.util.List;
import java.util.Optional;

public class ArrebentaBot implements BotServiceProvider {
    @Override
    public boolean getMaoDeOnzeResponse(GameIntel intel) {
        final List<TrucoCard> cards = intel.getCards();
        var vira = intel.getVira();
        int cardsValue = cards.stream().mapToInt(card -> card.relativeValue(vira)).sum();

        if((cardsValue < 15) && (intel.getOpponentScore() < 11)){
            return false;
        }

        return true;
    }



    @Override
    public boolean decideIfRaises(GameIntel intel) {
        return true;
    }

    @Override
    public CardToPlay chooseCard(GameIntel intel) {
        final List<TrucoCard> cards = intel.getCards();
        final Optional<TrucoCard> opponentCard = intel.getOpponentCard();
        var vira = intel.getVira();

        final Boolean hasManilhas = cards.stream().anyMatch(card -> card.isManilha(vira));
        int cardsValue = cards.stream().mapToInt(card -> card.relativeValue(vira)).sum();

        if(!intel.getRoundResults().isEmpty() && intel.getOpponentCard().isPresent()){
            if(!hasHigherCard(intel)){
                return CardToPlay.of(smallerCard(intel));
            }
        }

        if(!intel.getRoundResults().isEmpty() && intel.getRoundResults().get(0) == GameIntel.RoundResult.DREW)
            return CardToPlay.of(higherCard(intel));


        if(intel.getCards().size() > 2){
            if(!intel.getRoundResults().isEmpty() && intel.getOpponentCard().isPresent()){
                if(opponentCard.isPresent() && (opponentCard.get().relativeValue(vira) < middleCard(intel).relativeValue(vira)) &&
                        (opponentCard.get().relativeValue(vira) >= smallerCard(intel).relativeValue(vira))){
                    return CardToPlay.of(middleCard(intel));
                }
            }
        }

        if(intel.getCards().size() > 2){
            if(!intel.getRoundResults().isEmpty() && intel.getOpponentCard().isPresent()){
                if(opponentCard.isPresent() && (opponentCard.get().relativeValue(vira) >= middleCard(intel).relativeValue(vira))){
                    return CardToPlay.of(higherCard(intel));
                }
            }
        }

        if(intel.getCards().size() < 3){
            if((!intel.getRoundResults().isEmpty()) && (intel.getRoundResults().get(0) == GameIntel.RoundResult.WON)){
                return CardToPlay.of(higherCard(intel));
            }
        }


        return CardToPlay.of(smallerCard(intel));
    }

    @Override
    public int getRaiseResponse(GameIntel intel) {
        return 0;
    }

    @Override
    public String getName() {
        return BotServiceProvider.super.getName();
    }

    private TrucoCard higherCard(GameIntel intel) {
        var vira = intel.getVira();
        TrucoCard higherCard = null;
        for (TrucoCard card : intel.getCards()) {
            if (higherCard == null || card.relativeValue(vira) > higherCard.relativeValue(vira)) {
                higherCard = card;
            }
        }
        return higherCard;
    }

    private TrucoCard smallerCard(GameIntel intel) {
        var vira = intel.getVira();
        TrucoCard smallerCard = null;
        for (TrucoCard card : intel.getCards()) {
            if (smallerCard == null || card.relativeValue(vira) < smallerCard.relativeValue(vira)) {
                smallerCard = card;
            }
        }
        return smallerCard;
    }

    private TrucoCard middleCard(GameIntel intel) {
        var vira = intel.getVira();
        TrucoCard midleCard = null;
        final List<TrucoCard> cards = intel.getCards();
        for (TrucoCard card : cards) {
            if(midleCard == null && ((card.relativeValue(vira) <= higherCard(intel).relativeValue(vira)) && (card.relativeValue(vira) >= smallerCard(intel).relativeValue(vira)))){
                midleCard = card;
            }
        }
        return midleCard;
    }

    private boolean hasHigherCard(GameIntel intel){
        final List<TrucoCard> cards = intel.getCards();
        final Optional<TrucoCard> opponentCard = intel.getOpponentCard();
        var vira = intel.getVira();
        boolean hasHigher = false;
        for (TrucoCard card : intel.getCards()) {
            if(card.relativeValue(vira) > opponentCard.get().relativeValue(vira)){
                hasHigher = true;
            }
        }
        return hasHigher;
    }
}
