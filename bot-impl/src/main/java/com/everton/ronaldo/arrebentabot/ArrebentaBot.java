package com.everton.ronaldo.arrebentabot;

import com.bueno.spi.model.*;
import com.bueno.spi.service.BotServiceProvider;

import java.util.List;
import java.util.Optional;

public class ArrebentaBot implements BotServiceProvider {
    @Override
    public boolean getMaoDeOnzeResponse(GameIntel intel) {
        final List<TrucoCard> cards = intel.getCards();
        var vira = intel.getVira();
        int cardsValue = cards.stream().mapToInt(card -> card.relativeValue(vira)).sum();

        if ( intel.getOpponentScore() >= 9 && intel.getOpponentScore() < 11){
            if(cardsValue >= 22 || hasCasal(intel) || hasThrees(intel) || hasTwos(intel)) { return true; }
            return false;
        }
        if((cardsValue < 15) && (intel.getOpponentScore() < 11)){
            return false;
        }
        return true;
    }

    @Override
    public boolean decideIfRaises(GameIntel intel) {
        final List<TrucoCard> cards = intel.getCards();

        final boolean hasManilhas = cards.stream().anyMatch(card -> card.isManilha(intel.getVira()));
        int cardsValue = cards.stream().mapToInt(card -> card.relativeValue(intel.getVira())).sum();
        int handPoints = intel.getHandPoints();
        int score = intel.getScore();

        final Boolean hasCopas = cards.stream().anyMatch(card -> card.isCopas(intel.getVira()));
        final Boolean hasOuros = cards.stream().anyMatch(card -> card.isOuros(intel.getVira()));

        if(intel.getCards().size() == 3){

            if(hasCasal(intel) || hasThrees(intel) || hasTwos(intel)) { return true; }

            return false;
        }

        if(intel.getCards().size() == 1){
            if(hasManilhas || hasThree(intel) || hasTwo(intel) || hasAce(intel)){
                return true;
            }
            return false;
        }

        if(intel.getOpponentScore() == 11 || intel.getScore() == 11){ return false; }

        if(handPoints >= 3 ){ return false; }

        if(cardsValue < 18 && (!hasManilhas)){ return false; }

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
       final List<TrucoCard> cards = intel.getCards();
       var vira = intel.getVira();

       final Boolean hasManilhas = cards.stream().anyMatch(card -> card.isManilha(vira));
       int cardsValue = cards.stream().mapToInt(card -> card.relativeValue(vira)).sum();
       final Optional<TrucoCard> opponentCard = intel.getOpponentCard();

       if (intel.getCards().size() == 2) {
           if ((!intel.getRoundResults().isEmpty()) && (intel.getRoundResults().get(0) == GameIntel.RoundResult.LOST)) {
               if (!hasManilhas) {
                   if (cardsValue >= 12) {
                       return 1;
                   }
                   return 0;
               }
           }
       }

       if (intel.getCards().size() == 1) {

           if(intel.getOpponentScore() >= 9){
               if (hasManilhas || hasThree(intel) || hasTwo(intel)) {
                   return 1;
               }
               return 0;
           }
           if(intel.getOpponentScore() < 9){
               if (hasManilhas || hasThree(intel) || hasTwo(intel) || hasAce(intel)) {
                   return 1;
               }
           }
           return 0;
       }

       if (cardsValue >= 18 || hasManilhas) {
           return 1;
       }

       return 0;
   }

    @Override
    public String getName() {
        return BotServiceProvider.super.getName();
    }

    private boolean hasCasal(GameIntel intel) {
        List<TrucoCard> manilhas = intel.getCards()
                .stream()
                .filter(card -> card.isManilha(intel.getVira()))
                .toList();

        if(manilhas.size()>= 2){ return true; }
        return false;
    }

    private boolean hasThrees(GameIntel intel) {
        List<TrucoCard> threes = intel.getCards()
                .stream()
                .filter(card -> card.getRank().value() == 10)
                .toList();

        if(threes.size()>= 2){ return true; }
        return false;
    }

    private boolean hasTwos(GameIntel intel) {
        List<TrucoCard> twos = intel.getCards()
                .stream()
                .filter(card -> card.getRank().value() == 9)
                .toList();

        if(twos.size()>= 2){ return true; }
        return false;
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
            if(opponentCard.isPresent() && (card.relativeValue(vira) > opponentCard.get().relativeValue(vira))){
                hasHigher = true;
            }
        }
        return hasHigher;
    }

    private boolean hasThree(GameIntel intel){
        final List<TrucoCard> cards = intel.getCards();
        boolean hasThree = false;
        for (TrucoCard card : intel.getCards()) {
            if(card.getRank().value() == 10){
                hasThree = true;
            }
        }
        return hasThree;
    }

    private boolean hasTwo(GameIntel intel){
        final List<TrucoCard> cards = intel.getCards();
        boolean hasTwo = false;
        for (TrucoCard card : intel.getCards()) {
            if(card.getRank().value() == 9){
                hasTwo = true;
            }
        }
        return hasTwo;
    }

    private boolean hasAce(GameIntel intel){
        final List<TrucoCard> cards = intel.getCards();
        boolean hasAce = false;
        for (TrucoCard card : intel.getCards()) {
            if(card.getRank().value() == 8){
                hasAce = true;
            }
        }
        return hasAce;
    }
}
