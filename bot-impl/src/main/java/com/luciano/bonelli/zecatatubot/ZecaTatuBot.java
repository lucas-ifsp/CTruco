package com.luciano.bonelli.zecatatubot;

import com.bueno.spi.model.*;
import com.bueno.spi.service.BotServiceProvider;

import java.util.Comparator;
import java.util.Objects;

public class ZecaTatuBot implements BotServiceProvider {
    private TrucoCard highCard;
    private TrucoCard lowCard;

    @Override
    public boolean getMaoDeOnzeResponse(GameIntel intel) {
        int opponentScore = intel.getOpponentScore();
        if(opponentScore <= 2){
            return handValue(intel) >= 15;
        }
        else if(opponentScore <= 4){
            return handValue(intel) >= 19;
        }
        return false;
    }

    @Override
    public boolean decideIfRaises(GameIntel intel) {
        return false;
    }

    @Override
    public CardToPlay chooseCard(GameIntel intel) {

        boolean isFirstRound = intel.getRoundResults().isEmpty() || intel.getOpponentCard().isEmpty();


        if (isFirstRound) {
            TrucoCard cardToPlay = getHighCard(intel);
            return CardToPlay.of(cardToPlay);
        }

        TrucoCard cardToPlay = getLowCard(intel);
        return CardToPlay.of(cardToPlay);
    }

    @Override
    public int getRaiseResponse(GameIntel intel) {
        return 0;
    }

    public long countManilha (GameIntel intel) {
        return intel.getCards().stream()
                .filter(card -> card.isManilha(intel.getVira()))
                .count();
    }

    public int handValue(GameIntel intel){
        int handSValue = 0;
        for (TrucoCard card : intel.getCards()){
            handSValue += card.relativeValue(intel.getVira());
        }
        return handSValue;
    }

    public TrucoCard getHighCard(GameIntel intel) {
        TrucoCard vira = intel.getVira();
        this.highCard = intel.getCards().stream()
                .max(Comparator.comparingInt(card -> card.relativeValue(vira)))
                .orElse(null);
        return highCard;
    }

    public TrucoCard getLowCard(GameIntel intel) {
        TrucoCard vira = intel.getVira();
        this.lowCard = intel.getCards().stream()
                .min(Comparator.comparingInt(card -> card.relativeValue(vira)))
                .orElse(null);
        return lowCard;
    }


    public TrucoCard getMidCard(GameIntel intel) {
        return intel.getCards().stream()
                .filter(card -> !Objects.equals(card, highCard) && !Objects.equals(card, lowCard))
                .findFirst()
                .orElse(null);
    }

}
