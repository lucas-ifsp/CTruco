package com.gusmao.matos.clojurebot;

import com.bueno.spi.model.CardToPlay;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.service.BotServiceProvider;

public final class ClojureBot implements BotServiceProvider {
    @Override
    public boolean getMaoDeOnzeResponse(GameIntel intel) {
        final RoundStrategy strategy = Playing.buildStrategy(intel);

        return strategy.getMaoDeOnzeResponse(intel);
    }

    @Override
    public boolean decideIfRaises(GameIntel intel) {
        final RoundStrategy strategy = Playing.buildStrategy(intel);

        return strategy.decideIfRaises(intel);
    }

    @Override
    public CardToPlay chooseCard(GameIntel intel) {
        final RoundStrategy strategy = Playing.buildStrategy(intel);

        return strategy.chooseCard(intel);
    }

    @Override
    public int getRaiseResponse(GameIntel intel) {
        final RoundStrategy strategy = Playing.buildStrategy(intel);

        return strategy.getRaiseResponse(intel);
    }

    @Override
    public String getName() {
        return "The Devil \uD83D\uDE08";
    }
}
