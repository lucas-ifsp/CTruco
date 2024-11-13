package com.rafael.lucas.mestrimbot;

import com.bueno.spi.model.CardToPlay;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.service.BotServiceProvider;

public class Mestrim implements BotServiceProvider {
    @Override
    public boolean getMaoDeOnzeResponse(GameIntel intel) {
        return Strategy.of(intel).getMaoDeOnzeResponse(intel);
    }

    @Override
    public boolean decideIfRaises(GameIntel intel) {
        return Strategy.of(intel).decideIfRaises();
    }

    @Override
    public CardToPlay chooseCard(GameIntel intel) {
        return Strategy.of(intel).chooseCard();
    }

    @Override
    public int getRaiseResponse(GameIntel intel) {
        return Strategy.of(intel).getRaiseResponse();
    }
}
