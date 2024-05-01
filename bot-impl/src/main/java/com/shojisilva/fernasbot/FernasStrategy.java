package com.shojisilva.fernasbot;

import com.bueno.spi.model.CardRank;
import com.bueno.spi.model.CardToPlay;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public interface FernasStrategy {
    boolean getMaoDeOnzeResponse(GameIntel intel);
    boolean decideIfRaises(GameIntel intel);
    CardToPlay chooseCard(GameIntel intel);
    int getRaiseResponse(GameIntel intel);

    default List<TrucoCard> getCurrentCards(GameIntel intel){
        List<TrucoCard> cards = new ArrayList<>(intel.getCards());
        cards.sort((c1, c2) -> c1.compareValueTo(c2, intel.getVira()));
        return cards;
    }

    default Optional<TrucoCard> getMenorCarta(GameIntel intel){
        Optional<TrucoCard> opponentCard = intel.getOpponentCard();
        return opponentCard.flatMap(trucoCard -> getCurrentCards(intel)
                .stream()
                .filter(c -> c.compareValueTo(trucoCard, intel.getVira()) > 0)
                .findFirst());
    }

    default int getHandValue(GameIntel intel){
        return intel
                .getCards()
                .stream()
                .mapToInt(card -> card.relativeValue(intel.getVira()))
                .sum();
    }

    default boolean hasCasalMaior(GameIntel intel){
        return intel
                .getCards()
                .stream()
                .filter(c -> c.isZap(intel.getVira()) || c.isCopas(intel.getVira()))
                .toList()
                .size() == 2;
    }

    default boolean hasCasalMenor(GameIntel intel){
        return intel
                .getCards()
                .stream()
                .filter(c -> c.isEspadilha(intel.getVira()) || c.isOuros(intel.getVira()))
                .toList()
                .size() == 2;
    }

    default boolean hasCasalPreto(GameIntel intel){
        return intel
                .getCards()
                .stream()
                .filter(c -> c.isZap(intel.getVira()) || c.isEspadilha(intel.getVira()))
                .toList()
                .size() == 2;
    }

    default boolean hasCasalVermelho(GameIntel intel){
        return intel
                .getCards()
                .stream()
                .filter(c -> c.isCopas(intel.getVira()) || c.isOuros(intel.getVira()))
                .toList()
                .size() == 2;
    }

    default boolean hasPausOuros(GameIntel intel){
        return intel
                .getCards()
                .stream()
                .filter(c -> c.isOuros(intel.getVira()) || c.isZap(intel.getVira()))
                .toList()
                .size() == 2;
    }

    default boolean hasCopasEspadilha(GameIntel intel){
        return intel
                .getCards()
                .stream()
                .filter(c -> c.isOuros(intel.getVira()) || c.isZap(intel.getVira()))
                .toList()
                .size() == 2;
    }

    default int getAmountManilhas(GameIntel intel){
        return intel
                .getCards()
                .stream()
                .filter(c -> c.isManilha(intel.getVira()))
                .toList()
                .size();
    }


    default List<TrucoCard> getTres(List<TrucoCard> cards){
        return cards
                .stream()
                .filter(c -> c.getRank() == CardRank.THREE)
                .toList();
    }

    default Optional<TrucoCard> getManilha(GameIntel intel){
        return intel.getCards()
                .stream()
                .filter(c -> c.isManilha(intel.getVira()))
                .findFirst();
    }

    default int getAmountBetween(GameIntel intel, int ceil, int floor){
        return intel
                .getCards()
                .stream()
                .filter(c -> c.relativeValue(intel.getVira()) >= floor && c.relativeValue(intel.getVira()) <= ceil)
                .toList()
                .size();
    }
}