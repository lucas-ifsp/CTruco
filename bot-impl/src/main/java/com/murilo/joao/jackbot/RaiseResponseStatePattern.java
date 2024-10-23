package com.murilo.joao.jackbot;


import com.bueno.spi.model.GameIntel;

public interface RaiseResponseStatePattern {

    int getRaiseResponse (GameIntel intel);

    int handleFirstRound(GameIntel intel);

    int handleSecondRound(GameIntel intel);

    boolean hasStrongSecondRoundHand(GameIntel intel);

    boolean hasModerateSecondRoundHand(GameIntel intel);

    int handleThirdRound(GameIntel intel);
}
