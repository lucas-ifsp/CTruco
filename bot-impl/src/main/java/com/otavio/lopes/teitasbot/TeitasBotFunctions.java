package com.otavio.lopes.teitasbot;

import com.bueno.spi.model.CardRank;
import com.bueno.spi.model.CardSuit;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;


import java.util.List;

public class TeitasBotFunctions {

    static Boolean hasManilha(List<TrucoCard> cards, TrucoCard vira) {
        return cards.stream().anyMatch(card -> card.isManilha(vira));
    }

    static Boolean hasZap(List<TrucoCard> cards, TrucoCard vira){
        return cards.stream().anyMatch(card -> card.isZap(vira));
    }
    static Boolean hasThree(List<TrucoCard> cards) {
        return cards.stream().anyMatch(card -> card.getRank() == CardRank.THREE);
    }

    static Boolean hasNutsHand(List<TrucoCard> cards, TrucoCard vira) {
        //we have the best one. manilha + zap + 3.
        boolean hasZap = hasZap(cards, vira);
        boolean hasThree = hasThree(cards);
        boolean hasManilha =  hasManilha(cards, vira);

        return (hasManilha && hasThree && hasZap);
    }

    static Boolean hasGoodHand(List<TrucoCard> cards,TrucoCard vira){
        //we have manilha and 3.
        Boolean hasManilha = hasManilha(cards, vira);
        Boolean hasStrongCard = cards.stream().anyMatch(card -> card.getRank() == CardRank.THREE);
        return hasManilha & hasStrongCard;
    }
    static Boolean hasTrashHand(List<TrucoCard> cards,TrucoCard vira)
        //the worst.
    {
        Boolean hasManilha = hasManilha(cards,vira);
        Boolean hasStrongCard = cards.stream().anyMatch(card -> card.getRank() == CardRank.THREE);

        return !hasManilha && !hasStrongCard;

    }


    static boolean hasManilhaAlta( List<TrucoCard> cards,TrucoCard vira) {
        boolean hasManilha = hasManilha(cards, vira);
        if (hasManilha) {

            return cards.stream().anyMatch(card -> card.isZap(vira)) && cards.stream().anyMatch(card -> card.isCopas(vira));
        }

        return false;
    }

    static boolean hasStrongHand(List<TrucoCard> cards,TrucoCard vira){
        boolean hasManilhaAlta =  hasManilhaAlta(cards,vira);
        boolean hasManilhaAndThree =  hasManilha(cards, vira) & hasThree(cards);

        if (hasManilhaAndThree)
            return true;
        else return hasManilhaAlta;
    }

    static TrucoCard getWeakestCard(List<TrucoCard> cards, TrucoCard vira){

        TrucoCard wekeastCard = cards.get(0);
        for (TrucoCard card : cards) {
            if (card.compareValueTo(wekeastCard, vira) < 0) wekeastCard = card;
        }
        return wekeastCard;
    }

    static TrucoCard getStrongestCard(List<TrucoCard> cards, TrucoCard vira){
        TrucoCard strongestCard = cards.get(0);
        for (TrucoCard card : cards) {
            if (card.compareValueTo(strongestCard, vira) > 0) strongestCard = card;
        }

        return strongestCard;
    }

    static TrucoCard getMiddleCardLevel(List<TrucoCard> cards, TrucoCard vira){

        TrucoCard strongCard = getStrongestCard(cards,vira);
        TrucoCard weakestCard = getWeakestCard(cards, vira);

        for (TrucoCard card : cards){
            if (card != strongCard & card != weakestCard){
                return card;
            }
        }

        return null;
    }

    boolean firstRound(GameIntel intel){
        return intel.getRoundResults().isEmpty();
    }

    boolean secondRound(GameIntel intel){
        return intel.getRoundResults().size() == 1;
    }

    static boolean firstToPlay(GameIntel intel){
        return intel.getOpponentCard().isEmpty();
    }

    static Boolean PlayAgressiveMode(List<TrucoCard> cards, TrucoCard vira, GameIntel gameIntel){
        //if some of these is true. pression to the opponent


        Boolean first =  firstToPlay(gameIntel);
        Boolean hasGoodHand =  hasGoodHand(cards, vira);
        Boolean hasWeakHand =  hasTrashHand(cards, vira);

        return (hasGoodHand & first | hasWeakHand & first);
    }

    static Boolean PlayGoodMode(List<TrucoCard> cards, TrucoCard vira, GameIntel gameIntel){

        Boolean hasGoodHand =  hasGoodHand(cards,vira);

        return hasGoodHand(cards, vira);
    }
    static Boolean PlaySafeMood(List<TrucoCard> cards, TrucoCard vira, GameIntel gameIntel){
        Boolean hasNutsHand = hasNutsHand(cards, vira);
        Boolean isFirst = firstToPlay(gameIntel);
        Boolean hasStrongHand =  hasStrongHand(cards, vira);

        return hasNutsHand & isFirst | hasStrongHand & isFirst;
    }



}