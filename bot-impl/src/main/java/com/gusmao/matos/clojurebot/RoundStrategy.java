package com.gusmao.matos.clojurebot;

import com.bueno.spi.model.CardToPlay;

public interface RoundStrategy {
    default boolean getMaoDeOnzeResponse() {
        return false;
    }

    boolean decideIfRaises();

    CardToPlay chooseCard();

    int getRaiseResponse();
}
