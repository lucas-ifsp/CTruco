package com.antonelli.gibim.degolabot;

import com.bueno.spi.model.CardToPlay;
import com.bueno.spi.model.GameIntel;

public interface Strategy {
    int getRaiseResponse (GameIntel intel);

    boolean decideIfRaises (GameIntel intel);

    CardToPlay chooseCard (GameIntel intel);
}
