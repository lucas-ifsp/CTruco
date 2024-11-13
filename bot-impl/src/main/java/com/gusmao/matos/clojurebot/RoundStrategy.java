package com.gusmao.matos.clojurebot;

import com.bueno.spi.model.CardToPlay;

public interface RoundStrategy {
    boolean getMaoDeOnzeResponse();

    boolean decideIfRaises();

    CardToPlay chooseCard();

    int getRaiseResponse();
}
