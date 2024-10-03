package com.kayky.waleska.kwtruco;

import com.bueno.spi.service.BotServiceProvider;
import com.bueno.spi.model.*;
import com.bueno.spi.service.BotServiceProvider;

public class kwtruco implements BotServiceProvider {
    @Override
    public boolean getMaoDeOnzeResponse(GameIntel intel) {
        if (intel.getOpponentCard().stream().anyMatch(c-> c.isZap(intel.getVira()))){
            return false;
        }
        if (intel.getOpponentScore() == 11){
            return true;
        }
        if (intel.getOpponentScore() >= 9 ){
            return true;
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
    public String getName() {
        return BotServiceProvider.super.getName();
    }
}
