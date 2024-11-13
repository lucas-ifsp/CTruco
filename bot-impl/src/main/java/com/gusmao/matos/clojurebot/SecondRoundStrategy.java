package com.gusmao.matos.clojurebot;

import com.bueno.spi.model.CardToPlay;
import com.bueno.spi.model.GameIntel;

public final class SecondRoundStrategy implements RoundStrategy {
    private final GameIntel gameIntel;

    public SecondRoundStrategy(GameIntel gameIntel) {
        this.gameIntel = gameIntel;
    }

    @Override
    public boolean decideIfRaises() {
        return false;
    }

    @Override
    public CardToPlay chooseCard() {
        return null;
    }

    @Override
    public int getRaiseResponse() {
        return 0;
    }
}