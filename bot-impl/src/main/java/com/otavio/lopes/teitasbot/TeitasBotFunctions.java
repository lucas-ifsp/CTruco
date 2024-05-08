package com.otavio.lopes.teitasbot;

import com.bueno.spi.model.*;


import java.util.ArrayList;
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

    static CardToPlay chooseCardToPlayAtFirstStrongHand(List<TrucoCard> cards, TrucoCard vira) {
        //primeiro round e temos a ma forte.

        cards.sort((c1, c2) -> c2.compareValueTo(c1, vira));

        boolean isStrong =hasStrongHand(cards, vira);

        if (isStrong && cards.size() >= 2) {
            return CardToPlay.of(cards.get(1));
        }

        return CardToPlay.of(cards.get(0));
    };

    static CardToPlay chooseCardToPlaySecondIfWonFirst(List<TrucoCard> cards, TrucoCard vira) {

        List<TrucoCard> mutableCards = new ArrayList<>(cards);
        mutableCards.sort((c1, c2) -> c2.compareValueTo(c1, vira));
        return CardToPlay.of(mutableCards.get(0));

    }


    static CardToPlay cbooseCardToPlaySecondIfLooseFirst(List<TrucoCard> cards, TrucoCard vira) {
        //segundo round e temos a mao forte

        return chooseCardToPlaySecondIfWonFirst(cards, vira);

    }
    static CardToPlay chooseCardToPlayThird(List<TrucoCard> cards, TrucoCard vira) {
        return cbooseCardToPlaySecondIfLooseFirst(cards, vira);
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

    static Boolean hasTrashHand(List<TrucoCard> cards,TrucoCard vira){
        //the worst.
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

        return strongCard;
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

    static Boolean PlaySafeMood(List<TrucoCard> cards, TrucoCard vira, GameIntel gameIntel){
        // if we had nuts or strong hand, and is the first : play safe mode
        Boolean hasNutsHand = hasNutsHand(cards, vira);
        Boolean isFirst = firstToPlay(gameIntel);
        Boolean hasStrongHand =  hasStrongHand(cards, vira);

        return (isFirst && hasNutsHand) || (isFirst && hasStrongHand);


    }
}