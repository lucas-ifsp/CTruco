package com.igor_gabriel.botNovato;

import com.bueno.spi.model.*;
import com.bueno.spi.service.BotServiceProvider;



public class BotNovato implements BotServiceProvider {

    @Override
    public boolean getMaoDeOnzeResponse(GameIntel intel) {
        return handStrength(intel) > 21;
    }

    @Override
    public boolean decideIfRaises(GameIntel intel) {
        return countManilhas(intel) > 0;
    }

    @Override
    public CardToPlay chooseCard(GameIntel intel) {
        return CardToPlay.of(intel.getCards().get(0));
    }

    @Override
    public int getRaiseResponse(GameIntel intel) {
        return -1;
    }

    @Override
    public String getName() {
        return this.getClass().getSimpleName();
    }

    private int handStrength(GameIntel intel) {
        int strength = 0;
        for (TrucoCard card : intel.getCards()) {
            strength += card.relativeValue(intel.getVira());
        }
        return strength;
    }

    private int countManilhas(GameIntel intel) {
        int count = 0;
        for (TrucoCard card : intel.getCards()) {
            if (card.isManilha(intel.getVira())) {
                count++;
            }
        }
        return count;
    }
}

