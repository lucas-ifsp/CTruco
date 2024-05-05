package com.miguelestevan.jakaredumatubot;

import com.bueno.spi.model.CardSuit;
import com.bueno.spi.model.CardToPlay;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;
import com.bueno.spi.service.BotServiceProvider;

import java.util.List;

public class JakareDuMatuBot implements BotServiceProvider {
    @Override
    public boolean getMaoDeOnzeResponse(GameIntel intel) {
        return false;
    }

    @Override
    public boolean decideIfRaises(GameIntel intel) {

        if(intel.getHandPoints() == 12){
            return false;
        }

        switch (intel.getRoundResults().size()){
            case 0 -> {
                // First Hand
                if(getManilhas(intel.getCards(), intel.getVira()).containsAll(List.of(CardSuit.CLUBS, CardSuit.HEARTS))){
                    // Hand contains zap and copas
                    return true;
                } else if (getManilhas(intel.getCards(), intel.getVira()).size() == 2 && intel.getScore()-intel.getOpponentScore()>=3) {
                    return true;
                }
            }
            case 1 -> {
                // First Hand
                System.out.println("SECOND HAND");
            }
            case 2 -> {
                // First Hand
                System.out.println("THIRD HAND");
            }

        }


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

    @Override
    public String getName() {
        return "JakaréDuMatuBóty";
    }
    // The correct way to say is JakaréDuMatuBóty


    // This function returns a list of TrucoCards that are manilas
    private List<CardSuit> getManilhas(List<TrucoCard> botCards, TrucoCard vira) {
        return botCards.stream().filter(trucoCard -> trucoCard.isManilha(vira)).map(TrucoCard::getSuit).toList();
    }

}
