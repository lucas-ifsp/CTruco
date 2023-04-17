package com.bonelli.noli.paulistabot;

import com.bueno.spi.model.CardToPlay;
import com.bueno.spi.model.GameIntel;

public interface Strategy {

    int getRaiseResponse ();

    boolean getMaoDeOnzeResponse ();

    boolean decideIfRaises ();

    CardToPlay chooseCard ();
}
