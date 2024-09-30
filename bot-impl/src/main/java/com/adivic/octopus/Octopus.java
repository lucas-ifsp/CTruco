package com.adivic.octopus;

import com.bueno.spi.model.CardRank;
import com.bueno.spi.model.CardToPlay;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;
import com.bueno.spi.service.BotServiceProvider;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class Octopus implements BotServiceProvider {

    @Override
    public boolean getMaoDeOnzeResponse(GameIntel intel) {
        return false;
    }

    @Override
    public boolean decideIfRaises(GameIntel intel) {
        return false;
    }

    @Override
    public CardToPlay chooseCard(GameIntel intel) {
        return null;
    }

    @Override
    public int getRaiseResponse(GameIntel intel) {
        return 0;
    }

    public boolean hasManilha(GameIntel intel) {
        List<TrucoCard> cards = intel.getCards();
        TrucoCard vira = intel.getVira();

        return cards.stream()
                .limit(3)
                .anyMatch(card -> card.isManilha(vira));
    }

    public int numberOfManilhas(GameIntel intel){
        List<TrucoCard> cards = intel.getCards();
        TrucoCard vira = intel.getVira();

        return (int) cards.stream()
                .filter(card -> card.isManilha(vira))
                .count();
    }

    public List<TrucoCard> listOfManilhas(GameIntel intel){
        List<TrucoCard> cards = intel.getCards();
        TrucoCard vira = intel.getVira();

        if(hasManilha(intel)){
            return cards.stream()
                    .filter(card -> card.getRank() == vira.getRank()
                    .next()).collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    public boolean hasThree(GameIntel intel){

        List<TrucoCard> cards = intel.getCards();

        return cards.stream()
                .anyMatch(card -> card.getRank() == CardRank.THREE);
    }

    public int numberOfThreeCards(GameIntel intel){
        return 1;
    }

}
