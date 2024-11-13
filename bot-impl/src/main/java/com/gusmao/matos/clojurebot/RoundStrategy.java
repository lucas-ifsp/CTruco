package com.gusmao.matos.clojurebot;

import com.bueno.spi.model.CardToPlay;
import com.bueno.spi.model.GameIntel;

public interface RoundStrategy {
    boolean getMaoDeOnzeResponse(GameIntel intel);

    boolean decideIfRaises(GameIntel intel);

    CardToPlay chooseCard(GameIntel intel);

    int getRaiseResponse(GameIntel intel);
}
