package com.caua.felipe.cfbot;

import com.bueno.spi.model.*;
import com.bueno.spi.service.BotServiceProvider;

import java.util.List;

public class CFBot implements BotServiceProvider {


    @Override
    public int getRaiseResponse(GameIntel intel) {
        return 0;
    }

    @Override
    public boolean getMaoDeOnzeResponse(GameIntel intel) {
        List<TrucoCard> myCards = intel.getCards();

        if (isBiggestCoupleOrBlack(intel, myCards)) return true;
        if (toTwoOrThree(intel, myCards) && myCards.stream().anyMatch(card -> card.isZap(intel.getVira()))) return true;

        return false;
    }

    @Override
    public boolean decideIfRaises(GameIntel intel) {
        List<TrucoCard> myCards = intel.getCards();

        if (isBiggestCoupleOrBlack(intel, myCards)) return true;
        if (intel.getOpponentScore() > 7) return false;
        return toTwoOrThree(intel, myCards);
    }

    //To do

    @Override
    public CardToPlay chooseCard(GameIntel intel) {
//        int maxValue = intel.getCards().stream().map(TrucoCard::getRank).map(CardRank::value).max(Integer::compareTo).get();
//        TrucoCard vira =  intel.getVira();
//
//        List<TrucoCard> cards = intel.getCards();
//        int quantasManilha = cards.stream().filter(cart -> cart.isManilha(vira)).toList().size();
//
//        return CardToPlay.of(intel.getCards().stream().filter(card -> card.getRank().value() == maxValue).findAny().get());

        List<TrucoCard> cards = intel.getCards();

        TrucoCard cardMenor = cards.get(0);

        for (TrucoCard card : cards){
            if (card.compareValueTo(cardMenor, intel.getVira()) <  0) cardMenor = card;
        }


        if (whatRound(intel) == 1 && intel.getOpponentCard().isEmpty()){
            return CardToPlay.of(cardMenor);
        }

        return CardToPlay.of(intel.getCards().get(0));



    }






    public boolean isBiggestCoupleOrBlack(GameIntel gameIntel, List<TrucoCard> myCards) {

        boolean isManilhaClubs = myCards.stream().anyMatch(card -> card.isManilha(gameIntel.getVira()) && card.getSuit() == CardSuit.CLUBS);
        boolean isManilhaHearts = myCards.stream().anyMatch(card -> card.isManilha(gameIntel.getVira()) && card.getSuit() == CardSuit.HEARTS);
        boolean isManilhaSpades = myCards.stream().anyMatch(card -> card.isManilha(gameIntel.getVira()) && card.getSuit() == CardSuit.SPADES);

        if (isManilhaClubs && isManilhaHearts) return true;
        return isManilhaClubs && isManilhaSpades;
    }

    public boolean toTwoOrThree(GameIntel gameIntel, List<TrucoCard> myCards){
        return myCards.stream().anyMatch(card -> card.getRank().value() == 9 || card.getRank().value() == 10 );
    }

    public int whatRound(GameIntel gameIntel){
        List<TrucoCard> cards = gameIntel.getCards();

        if (cards.size() == 3) return 1;
        if (cards.size() == 2) return 2;
        return 3;
    }




}
