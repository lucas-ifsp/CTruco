package com.gusmao.matos.clojurebot;

import com.bueno.spi.model.CardToPlay;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.service.BotServiceProvider;

public final class ClojureBot implements BotServiceProvider {
    @Override
    public boolean getMaoDeOnzeResponse(GameIntel intel) {
        final RoundStrategy strategy = Playing.buildStrategy(intel);

        return strategy.getMaoDeOnzeResponse();
    }

    @Override
    public boolean decideIfRaises(GameIntel intel) {
        final RoundStrategy strategy = Playing.buildStrategy(intel);

        return strategy.decideIfRaises();
    }

    @Override
    public CardToPlay chooseCard(GameIntel intel) {
        final RoundStrategy strategy = Playing.buildStrategy(intel);

        return strategy.chooseCard();
    }

    @Override
    public int getRaiseResponse(GameIntel intel) {
        final RoundStrategy strategy = Playing.buildStrategy(intel);

        return strategy.getRaiseResponse();
    }

    @Override
    public String getName() {
        return "The Devil \uD83D\uDE08";
    }
}
