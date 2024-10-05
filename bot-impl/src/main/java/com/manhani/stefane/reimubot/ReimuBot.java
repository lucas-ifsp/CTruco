package com.manhani.stefane.reimubot;

import com.bueno.spi.model.CardRank;
import com.bueno.spi.model.CardToPlay;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.service.BotServiceProvider;

public class ReimuBot implements BotServiceProvider {
    public static final int REFUSE = -1;
    public static final int ACCEPT = 0;
    public static final int RERAISE = 1;
    
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
    
    private int getHandValue(GameIntel intel) {
        return intel.getCards().stream().mapToInt(c -> c.relativeValue(intel.getVira())).sum();
    }
    
    //should only be called after checking if you're not first
    private boolean canDefeatOpponentCard(GameIntel intel) {
        return intel.getCards().stream().anyMatch(c -> c.compareValueTo(intel.getOpponentCard().get(), intel.getVira()) > 0);
    }
    
    private boolean isFirstToPlayRound(GameIntel intel){
        return intel.getOpponentCard().isEmpty();
    }

}
