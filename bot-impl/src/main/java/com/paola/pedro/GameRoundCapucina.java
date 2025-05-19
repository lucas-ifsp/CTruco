package com.paola.pedro;

import com.bueno.spi.model.CardToPlay;
import com.bueno.spi.model.GameIntel;

public interface GameRoundCapucina {

    boolean decideIfRaises(GameIntel intel);

    int getRaiseResponse(GameIntel intel);

    CardToPlay chooseCard(GameIntel intel);
}
