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
        return hasManilha || hasStrongCard;
    }
    static Boolean hasTrashHand(List<TrucoCard> cards,TrucoCard vira)
    //the worst.
    {
        Boolean hasManilha = hasManilha(cards,vira);
        Boolean hasStrongCard = cards.stream().anyMatch(card -> card.getRank() == CardRank.THREE);

        return !hasManilha && !hasStrongCard;

    }
    static boolean isOpponentThatStartTheRound(GameIntel gameIntel) {

        return gameIntel.getOpponentCard().isPresent();
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


    //TODO indetify typefunciont

    static Boolean PlayAgressiveMode(List<TrucoCard> cards, TrucoCard vira, GameIntel gameIntel){
        //method that play at agressive mode
        Boolean nustHand = hasNutsHand(cards, vira);
        Boolean hasStrongCard =  hasStrongHand(cards,vira);
        Boolean weStart = isOpponentThatStartTheRound(gameIntel);
        if (nustHand & hasStrongCard & weStart){
            //logical porpose

        }
        else if (hasStrongCard & weStart){
            //logical porpose to play at agressive mode
        }
        else{
            return false;
        }

       return true;

    }


}