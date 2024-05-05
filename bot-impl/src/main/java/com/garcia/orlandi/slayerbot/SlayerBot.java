package com.garcia.orlandi.slayerbot;

import com.bueno.spi.model.CardToPlay;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.service.BotServiceProvider;

public class SlayerBot implements BotServiceProvider {
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
}
