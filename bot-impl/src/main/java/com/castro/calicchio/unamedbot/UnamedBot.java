package com.castro.calicchio.unamedbot;
import com.bueno.spi.model.CardToPlay;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;
import com.bueno.spi.service.BotServiceProvider;

import java.util.List;

public class UnamedBot implements BotServiceProvider{
    @Override
    public boolean getMaoDeOnzeResponse(GameIntel intel) {
        return true;
    }

    @Override
   public boolean decideIfRaises(GameIntel intel) {
    sortHand(intel);
    List<TrucoCard> cards = intel.getCards();
    int sum = cards.stream().mapToInt(TrucoCard::relativeValue).sum();
    int highValueCards = (int) cards.stream().filter(card -> card.relativeValue() > 9).count();
    int currentRound = intel.getCurrentRound();

    int raiseThreshold = 25;

    if (currentRound > 0) {
        raiseThreshold += currentRound * 5; 
    }

    raiseThreshold += highValueCards * 2;

    if (sum > raiseThreshold) {
        return 1; // Re-raise or call
    } else if (sum > 20) {
        return 0; // Accept raise
    } else {
        return -1; // Quit
    }
}


    @Override
    public CardToPlay chooseCard(GameIntel intel) {
        return null;
    }

    @Override
    public int getRaiseResponse(GameIntel intel) {
        return 0;
    }

    public void sortHand(GameIntel intel){
        List<TrucoCard> cards = intel.getCards();
        cards.sort(TrucoCard::relativeValue);
    }
}
