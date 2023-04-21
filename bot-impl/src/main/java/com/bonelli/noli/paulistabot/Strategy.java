package com.bonelli.noli.paulistabot;

import com.bueno.spi.model.CardToPlay;
import com.bueno.spi.model.GameIntel;

public interface Strategy {

    int getRaiseResponse (GameIntel intel);

    boolean getMaoDeOnzeResponse (GameIntel intel);

    boolean decideIfRaises (GameIntel intel);

    CardToPlay chooseCard (GameIntel intel);

    boolean hasManilha (GameIntel intel);
}
