package com.luciano.bonelli.zecatatubot;

import com.bueno.spi.model.*;
import com.bueno.spi.service.BotServiceProvider;

public class ZecaTatuBot implements BotServiceProvider {
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
}
