package com.breno.trucoJC.botBlessed;

import com.bueno.spi.model.*;
import com.bueno.spi.service.BotServiceProvider;

public class BotBlessed implements BotServiceProvider {
    @Override
    public boolean getMaoDeOnzeResponse(GameIntel intel) {
        boolean hasThree = intel.getCards().stream().anyMatch(c -> c.getRank() == CardRank.THREE);
        boolean hasManilha = intel.getCards().stream().anyMatch(c -> c.isManilha(intel.getVira()));
        long threeCount = intel.getCards().stream().filter(c -> c.getRank() == CardRank.THREE).count();

        if(intel.getScore() == 11 || intel.getOpponentScore() == 11){
            if (intel.getOpponentScore() - intel.getScore() <= 5 && hasThree) return true;
            if (hasManilha) return true;
            if (threeCount >= 2) return true;
        }
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

    @Override
    public String getName() { return "BotBlessedByJC";
    }
}
